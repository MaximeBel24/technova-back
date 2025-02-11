package fr.doranco.ecom_backend.repositories;

import fr.doranco.ecom_backend.models.Order;
import fr.doranco.ecom_backend.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
}
