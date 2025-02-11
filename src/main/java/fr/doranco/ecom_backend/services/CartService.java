package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Cart;
import fr.doranco.ecom_backend.models.CartProduct;

public interface CartService {

    public Cart getCartByUser(Long userId);

    public CartProduct addProductToCart(Long userId, Long productId, int quantity);

    public void removeProductFromCart(Long userId, Long productId);

    public void clearCart(Long userId);
}
