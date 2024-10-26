package com.ibiradopta.usersservice.repository;

import com.ibiradopta.usersservice.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository{

    private final Keycloak keycloakClient;
    @Value("${elaparato.keycloak.realm}")
    private String realm;


    @Override
    public List<User> findAll() {
        return keycloakClient.realm(realm).users().list().stream()
                .map(this::convertToUser).collect(Collectors.toList());
    }

    @Override
    public List<User> findByUserName(String userName) {
        List<UserRepresentation> userRepresentation = keycloakClient
                .realm(realm)
                .users()
                .search(userName);

        return userRepresentation.stream().map(this::convertToUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        UserRepresentation userRepresentation = keycloakClient.realm(realm).users().get(id).toRepresentation();
        return Optional.of(convertToUser(userRepresentation));
    }

    @Override
    public User deleteUserById(String id) {
        return null;
    }

    private User convertToUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .userName(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }
}
