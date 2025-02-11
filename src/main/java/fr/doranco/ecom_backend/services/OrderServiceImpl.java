package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.*;
import fr.doranco.ecom_backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public Order createOrder(Long userId) {

        Cart cart = cartRepository.findByUser(new User(userId, null, null, null, null, null, null, null, null))
                .orElseThrow(() -> new RuntimeException("Panier non trouvé !"));


        if (cart.getCartProducts().isEmpty()) {
            throw new RuntimeException("Le panier est vide, impossible de créer une commande.");
        }

        for (CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.getProduct().getStock() < cartProduct.getQuantity()) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + cartProduct.getProduct().getName());
            }
        }

        final Order order = orderRepository.save(
                Order.builder()
                        .user(cart.getUser())
                        .orderDate(new Date())
                        .status("pending")
                        .build()
        );

        // Convertir les produits du panier en produits de la commande
        Set<OrderProduct> orderProducts = cart.getCartProducts().stream()
                .map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    product.setStock(product.getStock() - cartProduct.getQuantity()); // 🔥 Décrémenter le stock
                    productRepository.save(product); // 🔥 Sauvegarder la nouvelle valeur du stock

                    return OrderProduct.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartProduct.getQuantity())
                            .price(product.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP))
                            .build();
                })
                .collect(Collectors.toSet());

        // Sauvegarde les produits de la commande
        orderProductRepository.saveAll(orderProducts);

        // Supprime les produits du panier après validation de la commande
//        cartProductRepository.deleteAll(cart.getCartProducts());
        cartService.clearCart(userId);

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
