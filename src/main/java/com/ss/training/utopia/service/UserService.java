package com.ss.training.utopia.service;

import com.ss.training.utopia.Exception.SQLAlreadyExistsException;
import com.ss.training.utopia.Exception.SQLDoesNotExistException;
import com.ss.training.utopia.dao.RoleDao;
import com.ss.training.utopia.dao.UserDao;
import com.ss.training.utopia.dto.UserDto;
import com.ss.training.utopia.entity.Role;
import com.ss.training.utopia.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // vars
    private final UserDao dao;
    private final RoleDao rdao;

    /**
     * Constructor
     * @param dao dao to use for Users
     */
    public UserService(UserDao dao, RoleDao rdao) {
        this.dao = dao;
        this.rdao = rdao;
    }


    /**
     * Convert DTO object to user
     * @param dto dto to convert
     * @return converted object
     */
    public User dtoToEntity(UserDto dto) {
        // check exists
        Optional<Role> role = rdao.findById(dto.getRoleId());
        if (role.isEmpty())
            throw new SQLDoesNotExistException("Role", String.valueOf(dto.getId()));

        // build
        return User.builder()
            .id(dto.getId())
            .role(role.get())
            .givenName(dto.getGivenName())
            .familyName(dto.getFamilyName())
            .username(dto.getUsername())
            .password(dto.getPassword())
            .email(dto.getEmail())
            .phone(dto.getPhone())
            .active(true)
            .build();
    }


    /**
     * Get all user types
     * @return list of users
     */
    public List<User> getAll() {
        return dao.findAll();
    }


    /**
     * Get user by ID
     * @param id id to search by
     * @return single User by ID
     */
    public User getUserById(Integer id) {
        Optional<User> user = dao.findById(id);
        if (user.isEmpty())
            throw new SQLDoesNotExistException("User", String.valueOf(id));
        return user.get();
    }

    /**
     * Get users by Role ID
     * @param roleId role to search by
     * @return list of users under a role
     */
    public List<User> getUsersByRole(Integer roleId) {
        Optional<Role> role = rdao.findById(roleId);
        if (role.isEmpty())
            throw new SQLDoesNotExistException("Role", String.valueOf(roleId));
        return dao.findAll(Example.of(User.builder()
        .role(role.get()).build()));
    }


    /**
     * Insert new user
     * @param insert user Dto to insert
     * @return copy of inserted user
     */
    public User add(UserDto insert) {
        User user = dtoToEntity(insert);
        if (dao.exists(Example.of(user)))
            throw new SQLAlreadyExistsException("User", user.getId() != null ? String.valueOf(user.getId()) : "none");
        return dao.save(user);
    }


    /**
     * Insert users from a list (single request)
     * Fails if there are any duplicate email/username/phones
     * @param insert list of DTOs to insert
     */
    @Transactional
    public void addList(List<UserDto> insert) {
        for (UserDto dto : insert) {
            User user = dtoToEntity(dto);
            // filter users who already satisfy uniqueness requirements
            if (!dao.existsByEmailOrUsernameOrPhone(user.getEmail(), user.getUsername(), user.getPhone()))
                dao.save(user);
        }
    }


    /**
     * Update existing user
     * @param insert Dto of user to update
     */
    public void update(UserDto insert) {
        User user = dtoToEntity(insert);
        if (!dao.existsById(user.getId()))
            throw new SQLDoesNotExistException("User", String.valueOf(user.getId()));
        dao.save(user);
    }


    /**
     * Remove existing user
     * @param id ID of user to remove
     */
    public void delete(Integer id) {
        Optional<User> user = dao.findById(id);
        if (user.isEmpty())
            throw new SQLDoesNotExistException("User", String.valueOf(id));
        dao.delete(user.get());
    }
}
