package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Role;
import fr.doranco.ecom_backend.models.User;

import java.util.List;

public interface AccountService {

    User addNewUser(User user);

    Role addNewRole(Role role);

    void addRolesToUser(User user, List<Role> roles);
}
