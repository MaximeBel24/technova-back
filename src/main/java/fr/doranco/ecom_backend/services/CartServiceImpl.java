package fr.doranco.ecom_backend.services;

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

    public Cart getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(null, user, null)));
    }

    public CartProduct addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé !"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuffisant pour le produit : " + product.getName());
        }

        Optional<CartProduct> existingCartProduct = cartProductRepository.findByCartAndProduct(cart, product);

        if (existingCartProduct.isPresent()) {
            CartProduct cartProduct = existingCartProduct.get();
            int newQuantity = cartProduct.getQuantity() + quantity;

            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + product.getName());
            }

            cartProduct.setQuantity(newQuantity);
            return cartProductRepository.save(cartProduct);
        } else {
            CartProduct newCartProduct = new CartProduct(null, cart, product, quantity);
            return cartProductRepository.save(newCartProduct);
        }
    }

    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé !"));

        cartProductRepository.findByCartAndProduct(cart, product).ifPresent(cartProductRepository::delete);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUser(userId);

        // Supprime toutes les entrées de CartProduct liées à ce panier
        cartProductRepository.deleteAll(cartProductRepository.findAllByCart(cart));

        // On vide la liste des produits dans l'objet Cart
        cart.getCartProducts().clear();
        cartRepository.save(cart);
    }
}
