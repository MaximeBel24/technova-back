package fr.doranco.ecom_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name ="product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du produit ne peut pas être vide.")
    @Size(max = 100, message = "Le nom du produit ne doit pas dépasser 100 caractères.")
    private String name;

    @NotBlank(message = "La description du produit ne peut pas être vide.")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Le prix est obligatoire.")
    @DecimalMin(value = "0.01", message = "Le prix du produit doit être supérieur à 0.")
    private BigDecimal price;

    @NotNull(message = "Le stock est obligatoire.")
    @Min(value = 0, message = "Le stock ne peut pas être négatif.")
    private Integer stock;

    @Column(length = 255)
    private String imageUrl;
}
