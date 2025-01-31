package fr.doranco.ecom_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;
}
