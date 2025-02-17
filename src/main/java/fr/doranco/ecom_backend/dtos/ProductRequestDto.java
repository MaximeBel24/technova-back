package fr.doranco.ecom_backend.dtos;

import fr.doranco.ecom_backend.models.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Category category;
}
