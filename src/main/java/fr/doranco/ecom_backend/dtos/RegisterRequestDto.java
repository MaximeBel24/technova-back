package fr.doranco.ecom_backend.dtos;

import fr.doranco.ecom_backend.models.Role;
import lombok.Data;

import java.util.Collection;

@Data
public class RegisterRequestDto {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
