package com.ss.training.utopia.dao;

import com.ss.training.utopia.entity.Role;
import com.ss.training.utopia.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Always ensure that there is both an 'admin' user and an 'ADMIN' role present in the database
 */
@Service
public class DbInit implements CommandLineRunner {

    // repos
    private final UserDao dao;
    private final RoleDao rdao;
    private final PasswordEncoder passwordEncoder;
    public DbInit(UserDao dao, RoleDao rdao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.rdao = rdao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ensure roles always exist
        Optional<Role> adminOpt = rdao.findByRoleName("ADMIN");
        Optional<Role> agentOpt = rdao.findByRoleName("AGENT");
        Optional<Role> userOpt = rdao.findByRoleName("USER");
        Role role;
        // admin
        if (adminOpt.isEmpty())
            role = Role.builder().id(3).roleName("ADMIN").build();
        else
            role = adminOpt.get();
        Role admin = rdao.save(role);
        // agent
        if (agentOpt.isEmpty())
            role = Role.builder().id(2).roleName("AGENT").build();
        else
            role = agentOpt.get();
        rdao.save(role);
        // user
        if (userOpt.isEmpty())
            role = Role.builder().id(1).roleName("USER").build();
        else
            role = userOpt.get();
        rdao.save(role);

        // ensure admin user exists
        if (!dao.existsByEmail("admin@foo.bar")) {
            User adminCreate = User.builder()
                .active(true)
                .email("admin@foo.bar")
                .familyName("bar")
                .givenName("foo")
                .username("admin")
                .password(passwordEncoder.encode("pass"))
                .phone("+1 (800) 888-8888")
                .role(role)
                .build();
            dao.save(adminCreate);
        }
    }
}
