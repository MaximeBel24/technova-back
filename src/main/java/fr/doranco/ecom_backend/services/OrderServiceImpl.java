package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Cart;
import fr.doranco.ecom_backend.models.Order;
import fr.doranco.ecom_backend.models.OrderProduct;
import fr.doranco.ecom_backend.models.User;
import fr.doranco.ecom_backend.repositories.CartProductRepository;
import fr.doranco.ecom_backend.repositories.CartRepository;
import fr.doranco.ecom_backend.repositories.OrderProductRepository;
import fr.doranco.ecom_backend.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderProductRepository orderProductRepository;

    public Order createOrder(Long userId) {
        // Récupère le panier de l'utilisateur
        Cart cart = cartRepository.findByUser(new User(userId, null, null, null, null, null, null, null, null))
                .orElseThrow(() -> new RuntimeException("Panier non trouvé !"));

        // Vérifie si le panier est vide
        if (cart.getCartProducts().isEmpty()) {
            throw new RuntimeException("Le panier est vide, impossible de créer une commande.");
        }

        // Créer et sauvegarder la commande immédiatement
        final Order order = orderRepository.save(
                Order.builder()
                        .user(cart.getUser())
                        .orderDate(new Date())
                        .status("pending")
                        .build()
        );

        // Convertir les produits du panier en produits de la commande
        Set<OrderProduct> orderProducts = cart.getCartProducts().stream()
                .map(cartProduct -> OrderProduct.builder()
                        .order(order) // Utilisation de `order` qui est maintenant final
                        .product(cartProduct.getProduct())
                        .quantity(cartProduct.getQuantity())
                        .price(cartProduct.getProduct().getPrice()) // Stocke le prix actuel
                        .build())
                .collect(Collectors.toSet());

        // Sauvegarde les produits de la commande
        orderProductRepository.saveAll(orderProducts);

        // Supprime les produits du panier après validation de la commande
        cartProductRepository.deleteAll(cart.getCartProducts());

        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUser(new User(userId, null, null, null, null, null, null, null, null));
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée !"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
