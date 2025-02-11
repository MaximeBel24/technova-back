package fr.doranco.ecom_backend.repositories;

import fr.doranco.ecom_backend.models.Order;
import fr.doranco.ecom_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
