package com.orderfulfillment;
import com.orderfulfillment.service.UserService;
import org.junit.jupiter.api.Test;
import com.orderfulfillment.entity.User;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import com.orderfulfillment.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
class OrderFulfillmentApplicationTests {
	@Autowired 
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
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
}
	
