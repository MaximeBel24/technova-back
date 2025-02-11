package fr.doranco.ecom_backend.services;

import fr.doranco.ecom_backend.models.Order;

import java.util.List;

public interface OrderService {

    public Order createOrder (Long userId);

    public List<Order> getUserOrders(Long userId);

    public Order updateOrderStatus(Long orderId, String newStatus);
}
