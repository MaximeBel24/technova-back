package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.exception.InvalidOperationException;
import fr.doranco.ecom_backend.exception.ResourceNotFoundException;
import fr.doranco.ecom_backend.models.Category;
import fr.doranco.ecom_backend.models.Product;
import fr.doranco.ecom_backend.repositories.CategoryRepository;
import fr.doranco.ecom_backend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + productId));
    }

    @Override
    public Product createProduct(Product product) {
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());

        if (category.isEmpty()) {
            throw new RuntimeException("Category not found");
        }

        product.setCategory(category.get());
        Product savedProduct = productRepository.save(product);

        // 🔥 Recharger le produit depuis la BDD pour éviter les problèmes de persistance
        return productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new RuntimeException("Product could not be saved"));
    }



    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product product = getProductById(productId);

        if (updatedProduct.getPrice() != null && updatedProduct.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Le prix du produit doit être supérieur à 0.");
        }
        if (updatedProduct.getStock() != null && updatedProduct.getStock() < 0) {
            throw new InvalidOperationException("Le stock ne peut pas être négatif.");
        }

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }
}
