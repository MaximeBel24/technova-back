package fr.doranco.ecom_backend.repositories;

import fr.doranco.ecom_backend.models.Category;
import fr.doranco.ecom_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory(Category category);
}
