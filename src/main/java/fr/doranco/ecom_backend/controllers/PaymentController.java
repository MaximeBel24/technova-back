package fr.doranco.ecom_backend.controllers;

import fr.doranco.ecom_backend.models.Payment;
import fr.doranco.ecom_backend.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<Payment> processPayment(@PathVariable Long orderId, @RequestParam String method) {
        return ResponseEntity.ok(paymentService.processPayment(orderId, method));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrder(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrder(orderId);
        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
