package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.exception.InvalidOperationException;
import fr.doranco.ecom_backend.exception.ResourceNotFoundException;
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

    @Override
    public Payment processPayment(Long orderId, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));

        if ("paid".equals(order.getStatus())) {
            throw new InvalidOperationException("Cette commande est déjà payée !");
        }

        Optional<Payment> existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment.isPresent()) {
            throw new InvalidOperationException("Un paiement existe déjà pour cette commande !");
        }

        String status = Math.random() > 0.2 ? "completed" : "failed";

        Payment payment = Payment.builder()
                .order(order)
                .amount(calculateTotalAmount(order))
                .method(method)
                .status(status)
                .paymentDate(new Date())
                .build();
        paymentRepository.save(payment);

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
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'ID : " + orderId));

        return paymentRepository.findByOrder(order)
                .or(() -> {
                    throw new ResourceNotFoundException("Aucun paiement trouvé pour la commande ID : " + orderId);
                });
    }
}
