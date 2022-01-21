package com.ss.training.utopia.service;

import com.ss.training.utopia.exception.SQLAlreadyExistsException;
import com.ss.training.utopia.exception.SQLDoesNotExistException;
import com.ss.training.utopia.dao.RoleDao;
import com.ss.training.utopia.dto.RoleDto;
import com.ss.training.utopia.entity.Role;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    // vars
    private final RoleDao dao;

    /**
     * Constructor
     * @param dao dao to use for Roles
     */
    public RoleService(RoleDao dao) {
        this.dao = dao;
    }

    /**
     * Convert DTO object to role
     * @param dto dto to convert
     * @return converted object
     */
    public Role dtoToEntity(RoleDto dto) {
        // build
        return Role.builder()
            .id(dto.getId())
            .roleName(dto.getRoleName())
            .build();
    }

    /**
     * Get all role types
     * @return list of roles
     */
    public List<Role> getAll() {
        return dao.findAll();
    }

    /**
     * Get role by ID
     * @param id id to search by
     * @return single Role by ID
     */
    public Role getRoleById(Integer id) {
        Optional<Role> role = dao.findById(id);
        if (role.isEmpty())
            throw new SQLDoesNotExistException("Role", String.valueOf(id));
        return role.get();
    }

    /**
     * Get role by Role Name
     * @param name name to search for
     * @return role if found
     */
    public Role getRoleByName(String name) {
        Optional<Role> role = dao.findOne(Example.of(Role.builder().id(null).roleName(name).build()));
        if (role.isEmpty())
            throw new SQLDoesNotExistException("Role", name);
        return role.get();
    }

    /**
     * Insert new role
     * @param insert role Dto to insert
     * @return copy of inserted role
     */
    public Role add(RoleDto insert) {
        Role role = dtoToEntity(insert);
        if (dao.existsById(role.getId()))
            throw new SQLAlreadyExistsException("Role", role.getId() != null ? String.valueOf(role.getId()) : "none");
        return dao.save(role);
    }

    /**
     * Update existing role
     * @param insert Dto of role to update
     */
    public void update(RoleDto insert) {
        Role role = dtoToEntity(insert);
        if (!dao.existsById(role.getId()))
            throw new SQLDoesNotExistException("Role", String.valueOf(role.getId()));
        dao.save(role);
    }

    /**
     * Remove existing role
     * @param id ID of role to remove
     */
    public void delete(Integer id) {
        Optional<Role> role = dao.findById(id);
        if (role.isEmpty())
            throw new SQLDoesNotExistException("Role", String.valueOf(id));
        dao.delete(role.get());
    }
}
