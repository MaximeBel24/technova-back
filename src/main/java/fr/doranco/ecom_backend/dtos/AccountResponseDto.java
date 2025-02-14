package fr.doranco.ecom_backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDto {
    private String message;
}
