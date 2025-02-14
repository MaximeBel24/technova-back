package fr.doranco.ecom_backend;

import fr.doranco.ecom_backend.enums.RoleName;
import fr.doranco.ecom_backend.models.Role;
import fr.doranco.ecom_backend.models.User;
import fr.doranco.ecom_backend.repositories.UserRepository;
import fr.doranco.ecom_backend.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class EcomBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomBackendApplication.class, args);
	}


}
