package fr.doranco.ecom_backend.controllers;

import fr.doranco.ecom_backend.dtos.AccountResponseDto;
import fr.doranco.ecom_backend.dtos.LoginRequestDto;
import fr.doranco.ecom_backend.dtos.RegisterRequestDto;
import fr.doranco.ecom_backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AccountResponseDto> register(@RequestBody RegisterRequestDto request){
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponseDto> login(@RequestBody LoginRequestDto request){
        return authenticationService.login(request);
    }

}
