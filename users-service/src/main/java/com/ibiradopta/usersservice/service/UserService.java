package com.ibiradopta.usersservice.service;

import com.ibiradopta.usersservice.model.User;
import com.ibiradopta.usersservice.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
