package com.ss.training.utopia.controller;

import com.ss.training.utopia.dto.UserDto;
import com.ss.training.utopia.entity.User;
import com.ss.training.utopia.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    // construction
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.of(Optional.ofNullable(service.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto dto) {
        User user = service.add(dto);
        URI uri = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto dto) {
        service.update(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
