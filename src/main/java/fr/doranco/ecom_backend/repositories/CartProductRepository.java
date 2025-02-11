package fr.doranco.ecom_backend.repositories;

import fr.doranco.ecom_backend.models.Cart;
import fr.doranco.ecom_backend.models.CartProduct;
import fr.doranco.ecom_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);

    List<CartProduct> findAllByCart(Cart cart);
}
