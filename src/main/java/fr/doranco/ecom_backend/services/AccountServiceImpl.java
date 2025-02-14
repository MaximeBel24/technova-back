package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.enums.RoleName;
import fr.doranco.ecom_backend.models.Role;
import fr.doranco.ecom_backend.models.User;
import fr.doranco.ecom_backend.repositories.RoleRepository;
import fr.doranco.ecom_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cet utilisateur n'existe pas"));
    }

    @Override
    public User addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = new Role(RoleName.USER);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(userRole);
        addRolesToUser(user, roles);

        return userRepository.save(user);

    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        User userFromDb = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Cet utilisateur n'existe pas"));

        roles.stream()
                .map(Role::getRoleName)
                .map(roleRepository::findByRoleName)
                .forEach(user.getRoles()::add);
    }
}
