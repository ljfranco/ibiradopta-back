package com.ibiradopta.usersservice.controller;

import com.ibiradopta.usersservice.model.User;
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


    @GetMapping("/getall")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/username/{userName}")
    @PreAuthorize("@userService.isAuthorized(authentication, #userName) or hasRole('ROLE_Administrador')")
    public ResponseEntity<List<User>> findByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(service.findByUserName(userName));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("#id == authentication.name or hasRole('ROLE_Administrador')")
    public ResponseEntity<User> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id).get());
    }

    @PatchMapping("/update")
    @PreAuthorize("#user.id == authentication.name or hasRole('ROLE_Administrador')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(service.updateUser(user));
    }

}
