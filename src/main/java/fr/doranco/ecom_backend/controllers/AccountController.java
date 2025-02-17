package fr.doranco.ecom_backend.controllers;

import fr.doranco.ecom_backend.dtos.ResponseDto;
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
    public ResponseEntity<ResponseDto> register(@RequestBody RegisterRequestDto request){
        System.out.println("🚀 Requête reçue pour /register : " + request);
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto request){
        System.out.println("🚀 Requête reçue pour /login : " + request);
        return authenticationService.login(request);
    }

}
