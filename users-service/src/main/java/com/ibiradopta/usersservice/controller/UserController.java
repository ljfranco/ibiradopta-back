package com.ibiradopta.usersservice.controller;

import com.ibiradopta.usersservice.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ibiradopta.usersservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;


    @Operation(summary = "Get all users", description = "Obtain all existing users, the respone is a list of users")
    @GetMapping("/getall")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Get an user by username", description = "Obtain an existing user, the respone is the user")
    @GetMapping("/username/{userName}")
    @PreAuthorize("@userService.isAuthorized(authentication, #userName) or hasRole('ROLE_Administrador')")
    public ResponseEntity<List<User>> findByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(service.findByUserName(userName));
    }

    @Operation(summary = "Get an user by Id", description = "Obtain an existing user, the respone is the user")
    @GetMapping("/id/{id}")
    @PreAuthorize("#id == authentication.name or hasRole('ROLE_Administrador')")
    public ResponseEntity<User> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id).get());
    }

    @Operation(summary = "Update an user", description = "Update an existing user, the respone is the updated user")
    @PatchMapping("/update")
    @PreAuthorize("#user.id == authentication.name or hasRole('ROLE_Administrador')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(service.updateUser(user));
    }

}
