package com.ibiradopta.usersservice.repository;

import com.ibiradopta.usersservice.exceptions.ResourceNotFoundException;
import com.ibiradopta.usersservice.model.User;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
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
    @Value("${ibiradopta.keycloak.realm}")
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

    @Override
    public User updateUser(User user) {
        UserResource userResource = keycloakClient.realm(realm).users().get(user.getId());
        UserRepresentation userRepresentation;
        try {
            // Intenta obtener la representación del usuario, lanza excepción si no existe
            userRepresentation = userResource.toRepresentation();
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found with id: " + user.getId());
        }
//        if (userRepresentation == null){
//            throw new ResourceNotFoundException("User not found with id: " + user.getId());
//        }
        try {
            if (user.getEmail() != null) {
                userRepresentation.setEmail(user.getEmail());
            }
            if (user.getFirstName() != null) {
                userRepresentation.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                userRepresentation.setLastName(user.getLastName());
            }
            if (user.getAddress() != null) {
                userRepresentation.getAttributes().put("address", List.of(user.getAddress()));
            }

            userResource.update(userRepresentation);
            return convertToUser(userRepresentation);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user with id: " + user.getId());
        }
    }

    private User convertToUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .userName(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .address(userRepresentation.getAttributes().get("address").get(0))
                .build();
    }
}
