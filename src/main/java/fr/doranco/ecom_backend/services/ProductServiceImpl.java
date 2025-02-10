package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Product;
import fr.doranco.ecom_backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setStock(productDetails.getStock());
                    existingProduct.setImageUrl(productDetails.getImageUrl());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé !"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
