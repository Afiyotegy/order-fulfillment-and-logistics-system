package com.orderfulfillment.service;

import org.springframework.stereotype.Service;
import com.orderfulfillment.repository.OrderRepository;
import com.orderfulfillment.entity.Order;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = getOrderById(id);

        existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setCustomerEmail(updatedOrder.getCustomerEmail());
        existingOrder.setCustomerPhone(updatedOrder.getCustomerPhone());
        existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        existingOrder.setShippingAddressSnapshot(updatedOrder.getShippingAddressSnapshot());
        existingOrder.setOrderDate(updatedOrder.getOrderDate());
        existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
        existingOrder.setStatus(updatedOrder.getStatus());
        existingOrder.setNotes(updatedOrder.getNotes());

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
