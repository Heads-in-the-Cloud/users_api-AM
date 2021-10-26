package com.ss.training.utopia.controller;

import com.ss.training.utopia.dto.RoleDto;
import com.ss.training.utopia.entity.Role;
import com.ss.training.utopia.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    // construction
    private final RoleService service;
    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = service.getAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable int id) {
        return ResponseEntity.of(Optional.ofNullable(service.getRoleById(id)));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        return ResponseEntity.of(Optional.ofNullable(service.getRoleByName(name)));
    }

    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody RoleDto dto) {
        Role role = service.add(dto);
        URI uri = URI.create("/api/v1/roles/" + role.getId());
        return ResponseEntity.created(uri).body(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@RequestBody RoleDto dto) {
        service.update(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
