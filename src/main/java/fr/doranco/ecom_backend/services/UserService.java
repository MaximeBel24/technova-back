package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> getUserByEmail(String email);
}
