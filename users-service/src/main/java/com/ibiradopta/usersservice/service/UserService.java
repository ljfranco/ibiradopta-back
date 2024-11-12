package com.ibiradopta.usersservice.service;

import com.ibiradopta.usersservice.model.User;
import com.ibiradopta.usersservice.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository repository;

    public List<User> findAll() {

        return repository.findAll();
    }

    public List<User> findByUserName(String userName) {

        return repository.findByUserName(userName);
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    public boolean isAuthorized(Authentication authentication, String userName) {
        String currentUserName = getCurrentUserName(authentication);
        return currentUserName.equals(userName);
    }

    private String getCurrentUserName(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username"); // Cambia "preferred_username" por el claim que almacena el nombre de usuario
        }
        return null;
    }

}
