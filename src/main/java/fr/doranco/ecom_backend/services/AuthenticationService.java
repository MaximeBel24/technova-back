package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.dtos.AccountResponseDto;
import fr.doranco.ecom_backend.dtos.LoginRequestDto;
import fr.doranco.ecom_backend.dtos.RegisterRequestDto;
import fr.doranco.ecom_backend.enums.RoleName;
import fr.doranco.ecom_backend.models.Role;
import fr.doranco.ecom_backend.models.User;
import fr.doranco.ecom_backend.repositories.RoleRepository;
import fr.doranco.ecom_backend.repositories.UserRepository;
import fr.doranco.ecom_backend.security.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ResponseEntity<AccountResponseDto> register(RegisterRequestDto request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(AccountResponseDto.builder()
                            .message("Un utilisateur existe déjà avec cette adresse email")
                            .build());
        }

        Role userRole = roleRepository.findByRoleName(RoleName.USER);

        List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(userRole))
                .build();



        userRepository.save(user);

        return ResponseEntity.ok(AccountResponseDto.builder()
                .message("Utilisateur enregistré avec succès")
                .build());
    }

    public ResponseEntity<AccountResponseDto> login(LoginRequestDto request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Utilisateur introubale"));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtService.generateToken(user);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Expose-Headers", "Authorization");
        responseHeaders.add("Authorization", "Bearer " + jwtToken);


        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(AccountResponseDto.builder()
                        .message("Utilisateur authentifié avec succès")
                        .build());
    }
}
