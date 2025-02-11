package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.exception.InvalidOperationException;
import fr.doranco.ecom_backend.exception.ResourceNotFoundException;
import fr.doranco.ecom_backend.models.Cart;
import fr.doranco.ecom_backend.models.CartProduct;
import fr.doranco.ecom_backend.models.Product;
import fr.doranco.ecom_backend.models.User;
import fr.doranco.ecom_backend.repositories.CartProductRepository;
import fr.doranco.ecom_backend.repositories.CartRepository;
import fr.doranco.ecom_backend.repositories.ProductRepository;
import fr.doranco.ecom_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUser(new User(userId, null, null, null, null, null, null, null, null))
                .orElseThrow(() -> new ResourceNotFoundException("Panier non trouvé pour l'utilisateur ID : " + userId));
    }

    @Override
    public CartProduct addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + productId));

        // Vérifier que le stock est suffisant
        if (product.getStock() < quantity) {
            throw new InvalidOperationException("Stock insuffisant pour le produit : " + product.getName());
        }

        Optional<CartProduct> existingCartProduct = cartProductRepository.findByCartAndProduct(cart, product);

        if (existingCartProduct.isPresent()) {
            CartProduct cartProduct = existingCartProduct.get();
            int newQuantity = cartProduct.getQuantity() + quantity;

            if (product.getStock() < newQuantity) {
                throw new InvalidOperationException("Stock insuffisant pour ajouter " + quantity + " exemplaires de " + product.getName());
            }

            cartProduct.setQuantity(newQuantity);
            return cartProductRepository.save(cartProduct);
        } else {
            CartProduct newCartProduct = new CartProduct(null, cart, product, quantity);
            return cartProductRepository.save(newCartProduct);
        }
    }

    @Override
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + productId));

        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new InvalidOperationException("Le produit n'est pas dans le panier."));

        cartProductRepository.delete(cartProduct);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUser(userId);

        if (cart.getCartProducts().isEmpty()) {
            throw new InvalidOperationException("Le panier est déjà vide.");
        }

        cartProductRepository.deleteAll(cart.getCartProducts());
    }
}
