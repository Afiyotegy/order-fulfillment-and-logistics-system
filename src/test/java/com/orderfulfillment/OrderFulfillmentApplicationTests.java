package com.orderfulfillment;
import com.orderfulfillment.service.UserService;
import org.junit.jupiter.api.Test;
import com.orderfulfillment.entity.OrderStatus;
import com.orderfulfillment.entity.Order;
import com.orderfulfillment.entity.User;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import com.orderfulfillment.repository.OrderRepository;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.orderfulfillment.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.orderfulfillment.service.OrderService;

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

	@Test
	void contextLoads() {
	}

	@Test 
	void shouldSaveUser() {
		User user = new User();

		user.setName("John Smith");
		user.setEmail("john@example.com");
		user.setPassword("password123");
		user.setRole("ADMIN");

		User savedUser = userService.saveUser(user);

		assertNotNull(savedUser.getId());
	}

	@Test 
	void shouldRetrieveUserById() {
		User user = new User();
		user.setName("Jane Smith");
		user.setEmail("jane@example.com");
		user.setPassword("password123");
		user.setRole("WAREHOUSE");

		User savedUser = userService.saveUser(user);
		User retrievedUser = userService.getUserById(savedUser.getId());

		assertEquals("jane@example.com", retrievedUser.getEmail());
	}

	@Test 
	void shouldRetrieveAllUsers() {
		User user = new User();

		user.setName("David Smith");
		user.setEmail("david@example.com");
		user.setPassword("password123");
		user.setRole("SHIPPER");
		userService.saveUser(user);
		List<User> users = userService.getAllUsers();

		assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("david@example.com")));
	}

	@Test
	void shouldDeleteUser() {
		User user = new User();

		user.setName("Michael Smith");
		user.setEmail("michael@example.com");
		user.setPassword("password123");
		user.setRole("SHIPPER");

		User savedUser = userService.saveUser(user);
		userService.deleteUser(savedUser.getId());
		assertFalse(userRepository.existsById(savedUser.getId()));
	}

	@Test 
	void shouldSaveOrderWithUser() {
		User user = new User();
		user.setName("Alex Smith");
		user.setEmail("alex@example.com");
		user.setPassword("password123");
		user.setRole("WAREHOUSE");
		
		User savedUser = userService.saveUser(user);

		Order order = new Order();
		order.setUser(savedUser);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalAmount(new BigDecimal("1599.44"));
		order.setStatus(OrderStatus.PENDING);

		Order savedOrder = orderRepository.save(order);
		assertNotNull(savedOrder.getId());
		assertEquals(savedUser.getId(), savedOrder.getUser().getId());
	}

	@Test
	void shouldCreateOrder() {
    User user = new User();
    user.setName("Robert Smith");
    user.setEmail("robert@example.com");
    user.setPassword("password123");
    user.setRole("WAREHOUSE");

    User savedUser = userService.saveUser(user);

    Order order = new Order();
    order.setUser(savedUser);
    order.setOrderDate(LocalDateTime.now());
    order.setTotalAmount(new BigDecimal("1599.44"));
    order.setStatus(OrderStatus.PENDING);

    Order savedOrder = orderService.createOrder(order);

    assertNotNull(savedOrder.getId());
    assertEquals(savedUser.getId(), savedOrder.getUser().getId());
	}

	@Test
	void shouldRetrieveAllOrders() {
    User user = new User();
    user.setName("Robert Smith");
    user.setEmail("robert@example.com");
    user.setPassword("password123");
    user.setRole("WAREHOUSE");

    User savedUser = userService.saveUser(user);

    Order order = new Order();
    order.setUser(savedUser);
    order.setOrderDate(LocalDateTime.now());
    order.setTotalAmount(new BigDecimal("2499.00"));
    order.setStatus(OrderStatus.PENDING);

    orderService.createOrder(order);

    List<Order> orders = orderService.getAllOrders();

    assertTrue(orders.stream()
            .anyMatch(o -> o.getTotalAmount().equals(new BigDecimal("2499.00"))));
	}

	@Test 
	void shouldRetrieveOrderById() {
		User user = new User();
		user.setName("Sarah Smith");
		user.setEmail("sarah@example.com");
		user.setPassword("password123");
		user.setRole("WAREHOUSE");

		User savedUser = userService.saveUser(user);

		Order order = new Order();
		order.setUser(savedUser);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalAmount(new BigDecimal("2499.00"));
		order.setStatus(OrderStatus.PENDING);

		Order savedOrder = orderService.createOrder(order);

		Order retrievedOrder = orderService.getOrderById(savedOrder.getId());

		assertEquals(new BigDecimal("2499.00"), retrievedOrder.getTotalAmount());
		assertEquals(savedOrder.getId(), retrievedOrder.getId());
	}

	@Test 
	void shouldUpdateOrder() {
		User user = new User();
		user.setName("Daniel Smith");
		user.setEmail("daniel@example.com");
		user.setPassword("password123");
		user.setRole("WAREHOUSE");

		User savedUser = userService.saveUser(user);

		Order order = new Order();
		order.setUser(savedUser);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalAmount(new BigDecimal("2499.00"));
		order.setStatus(OrderStatus.PENDING);

		Order savedOrder = orderService.createOrder(order);

		savedOrder.setTotalAmount(new BigDecimal("2999.00"));
		savedOrder.setStatus(OrderStatus.PROCESSING);

		Order updatedOrder = orderService.updateOrder(savedOrder.getId(), savedOrder);

		assertEquals(new BigDecimal("2999.00"), updatedOrder.getTotalAmount());
		assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
	}

	@Test 
	void shouldDeleteOrder() {
		User user = new User();
		user.setName("Emma Smith");
		user.setEmail("emma@example.com");
		user.setPassword("password123");
		user.setRole("WAREHOUSE");

		User savedUser = userService.saveUser(user);

		Order order = new Order();
		order.setUser(savedUser);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalAmount(new BigDecimal("1500.00"));
		order.setStatus(OrderStatus.PENDING);

		Order savedOrder = orderService.createOrder(order);

		orderService.deleteOrder(savedOrder.getId());

		assertFalse(orderRepository.existsById(savedOrder.getId()));
	}
}
	
