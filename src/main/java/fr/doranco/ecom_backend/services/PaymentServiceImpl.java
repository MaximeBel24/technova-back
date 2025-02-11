package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Order;
import fr.doranco.ecom_backend.models.Payment;
import fr.doranco.ecom_backend.repositories.OrderRepository;
import fr.doranco.ecom_backend.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    public Payment processPayment(Long orderId, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée !"));

        if (order.getStatus().equals("paid")) {
            throw new RuntimeException("Cette commande est déjà payée !");
        }

        // Simuler un paiement réussi ou échoué
        String status = Math.random() > 0.2 ? "completed" : "failed"; // 80% de succès, 20% d'échec

        // Créer et enregistrer le paiement
        Payment payment = Payment.builder()
                .order(order)
                .amount(calculateTotalAmount(order)) // Calcul du total
                .method(method)
                .status(status)
                .paymentDate(new Date())
                .build();
        paymentRepository.save(payment);

        // Mettre à jour le statut de la commande si paiement réussi
        if ("completed".equals(status)) {
            order.setStatus("paid");
            orderRepository.save(order);
        }

        return payment;
    }

    private BigDecimal calculateTotalAmount(Order order) {
        return order.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Optional<Payment> getPaymentByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée !"));
        return paymentRepository.findByOrder(order);
    }
}
