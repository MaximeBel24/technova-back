package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<Product> getAllProducts();

    public Optional<Product> getProductById(Long id);

    public Product saveProduct(Product product);

    public Product updateProduct(Long id, Product productDetails);

    public void deleteProduct(Long id);
}
