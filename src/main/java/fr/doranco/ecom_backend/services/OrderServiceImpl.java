package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.exception.InvalidOperationException;
import fr.doranco.ecom_backend.exception.ResourceNotFoundException;
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
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public Order createOrder(Long userId) {

        Cart cart = cartRepository.findByUser(new User(userId, null, null, null, null, null, null, null, null))
                .orElseThrow(() -> new ResourceNotFoundException("Panier non trouvé pour l'utilisateur ID : " + userId));

        if (cart.getCartProducts().isEmpty()) {
            throw new InvalidOperationException("Le panier est vide, impossible de créer une commande.");
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

        Set<OrderProduct> orderProducts = cart.getCartProducts().stream()
                .map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    product.setStock(product.getStock() - cartProduct.getQuantity());
                    productRepository.save(product);

                    return OrderProduct.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartProduct.getQuantity())
                            .price(product.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP))
                            .build();
                })
                .collect(Collectors.toSet());

        orderProductRepository.saveAll(orderProducts);
        cartService.clearCart(userId);

        return order;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        List<Order> orders = orderRepository.findByUser(user);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Aucune commande trouvée pour l'utilisateur ID : " + userId);
        }

        return orders;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));

        if ("delivered".equals(order.getStatus())) {
            throw new InvalidOperationException("Impossible de modifier une commande déjà livrée !");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
