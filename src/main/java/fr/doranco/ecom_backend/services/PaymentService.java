package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Payment;

import java.util.Optional;

public interface PaymentService {

    public Payment processPayment(Long orderId, String method);

    public Optional<Payment> getPaymentByOrder(Long orderId);
}
