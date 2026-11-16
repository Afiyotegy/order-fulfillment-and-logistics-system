package com.orderfulfillment;
import com.orderfulfillment.entity.Role;
import com.orderfulfillment.entity.Order;
import com.orderfulfillment.entity.OrderStatus;
import com.orderfulfillment.entity.User;
import com.orderfulfillment.repository.OrderRepository;
import com.orderfulfillment.repository.UserRepository;
import com.orderfulfillment.service.OrderService;
import com.orderfulfillment.service.UserService;
import com.orderfulfillment.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.orderfulfillment.repository.AddressRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class OrderFulfillmentApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressRepository addressRepository;
    @Test
    void contextLoads() {
    }

    // USER TESTS

    @Test
    void shouldSaveUser() {
        String email = "john-" + java.util.UUID.randomUUID() + "@example.com";

        User user = createTestUser(email);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser.getId());
        assertEquals(email, savedUser.getEmail());
    }

    @Test
    void shouldRetrieveUserById() {
        User user = createTestUser("jane@example.com");

        User savedUser = userService.saveUser(user);
        User retrievedUser = userService.getUserById(savedUser.getId());

        assertEquals("jane@example.com", retrievedUser.getEmail());
    }

    @Test
    void shouldRetrieveAllUsers() {
        User user = createTestUser("david@example.com");

        userService.saveUser(user);

        List<User> users = userService.getAllUsers();

        assertTrue(users.stream()
                .anyMatch(u -> u.getEmail().equals("david@example.com")));
    }

    @Test
    void shouldDeleteUser() {
        User user = createTestUser("michael@example.com");

        User savedUser = userService.saveUser(user);

        userService.deleteUser(savedUser.getId());

        assertFalse(userRepository.existsById(savedUser.getId()));
    }

    // ORDER TESTS

    @Test
    void shouldSaveOrderWithCustomerDetails() {
        Order order = createTestOrder("ORD-001",
                new BigDecimal("1599.44"));

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals("ORD-001", savedOrder.getOrderNumber());
        assertEquals("Alex Smith", savedOrder.getCustomerName());
        assertEquals("alex@example.com", savedOrder.getCustomerEmail());
    }

    @Test
    void shouldCreateOrder() {
        Order order = createTestOrder("ORD-002",
                new BigDecimal("1599.44"));

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder.getId());
        assertEquals("ORD-002", savedOrder.getOrderNumber());
    }

    @Test
    void shouldRetrieveAllOrders() {
        Order order = createTestOrder("ORD-003",
                new BigDecimal("2499.00"));

        orderService.createOrder(order);

        List<Order> orders = orderService.getAllOrders();

        assertTrue(orders.stream()
                .anyMatch(o -> o.getOrderNumber().equals("ORD-003")));
    }

    @Test
    void shouldRetrieveOrderById() {
        Order order = createTestOrder("ORD-004",
                new BigDecimal("2499.00"));

        Order savedOrder = orderService.createOrder(order);

        Order retrievedOrder = orderService.getOrderById(savedOrder.getId());

        assertEquals("ORD-004", retrievedOrder.getOrderNumber());
        assertEquals(new BigDecimal("2499.00"),
                retrievedOrder.getTotalAmount());
    }

    @Test
    void shouldUpdateOrder() {
        Order order = createTestOrder("ORD-005",
                new BigDecimal("2499.00"));

        Order savedOrder = orderService.createOrder(order);

        savedOrder.setTotalAmount(new BigDecimal("2999.00"));
        savedOrder.setStatus(OrderStatus.PROCESSING);
        savedOrder.setNotes("Updated order");

        Order updatedOrder = orderService.updateOrder(
                savedOrder.getId(), savedOrder);

        assertEquals(new BigDecimal("2999.00"),
                updatedOrder.getTotalAmount());
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
        assertEquals("Updated order", updatedOrder.getNotes());
    }

    @Test
    void shouldDeleteOrder() {
        Order order = createTestOrder("ORD-006",
                new BigDecimal("1500.00"));

        Order savedOrder = orderService.createOrder(order);

        orderService.deleteOrder(savedOrder.getId());

        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }

    // HELPER METHODS

    private User createTestUser(String email) {
        User user = new User();

        user.setName("Test User");
        user.setEmail(email);
        user.setPasswordHash("test-hash");
        user.setRole(Role.CUSTOMER);
        user.setPhone("9876543210");

        return user;
    }

    private Order createTestOrder(String orderNumber, BigDecimal amount) {

        User user = createTestUser("alex@example.com");
        User savedUser = userRepository.save(user);

        Address address = new Address();
        address.setUser(savedUser);
        address.setAddressType("HOME");
        address.setRecipientName("Alex Smith");
        address.setPhone("9876543210");
        address.setAddressLine1("123 Main Street");
        address.setCity("Kochi");
        address.setState("Kerala");
        address.setPostalCode("682001");
        address.setCountry("India");

        Address savedAddress = addressRepository.save(address);

        Order order = new Order();

        order.setOrderNumber(orderNumber);
        order.setUser(savedUser);
        order.setShippingAddress(savedAddress);
        order.setCustomerName("Alex Smith");
        order.setCustomerEmail("alex@example.com");
        order.setCustomerPhone("9876543210");
        order.setShippingAddressSnapshot("123 Main Street, Kochi, Kerala, 682001, India");
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(amount);
        order.setStatus(OrderStatus.PENDING);
        order.setNotes("Test order");

        return order;
    }
}
