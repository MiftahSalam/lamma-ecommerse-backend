package com.miftah.lamaecommerse.services.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.models.cart.Cart;
import com.miftah.lamaecommerse.models.cart.CartItem;
import com.miftah.lamaecommerse.models.product.Product;
import com.miftah.lamaecommerse.services.product.ProductService;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;
import com.miftah.lamaecommerse.shared.product.ProductMockData;
import com.miftah.lamaecommerse.shared.user.UserMockData;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CartServiceTest {
	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserServiceImpl userService;

	private List<Cart> carts = new ArrayList<>();
	
	private Set<CartItem> cart1Items = new HashSet<>();

	@BeforeAll
	void init() {
		boolean initOk = true;
		int i = 0;
		for (User user : UserMockData.userList) {
			log.info("create user {}", user.getUsername());

			try {
				userService.create(user);

				user.setBaseId(user.getId());
				UserMockData.userList.set(i, user);
				User userUpdate = UserMockData.userListUpdate.get(i);
				userUpdate.setId(user.getId());
				userUpdate.setBaseId(user.getId());
				UserMockData.userListUpdate.set(i, userUpdate);
				i++;
			} catch (Exception e) {
				initOk = false;
			}
		}
		assumeTrue(initOk);

		i = 0;
		try {
			for (Product product : ProductMockData.products) {
				log.info("Create product {}", product);
				Product createProduct = productService.create(product);

				assertThat(createProduct.getName()).isEqualTo(product.getName());
				createProduct.setBaseId(createProduct.getId());
				ProductMockData.products.set(i, createProduct);
				i++;
			}
		} catch (Exception e) {
			initOk = false;
		}
		assumeTrue(initOk);
	}

	@Test
//	@Disabled
	@Order(1)
	void testCreateCart_Success() {
		assertThatNoException().isThrownBy(() -> {
			User user = UserMockData.userList.get(0);
			Product product1 = ProductMockData.products.get(0);
			Product product2 = ProductMockData.products.get(1);
			CartItem item1 = new CartItem(product1, 3);
			CartItem item2 = new CartItem(product2, 4);
			
			Cart cart = new Cart();

			cart1Items.addAll(Set.of(item1, item2));
			cart.addItems(cart1Items);
			cart.setUser(user);
			this.carts.add(cart);
			
			Cart createdCart = cartService.createCart(cart);
			double total = 0.;
			for (CartItem item: this.cart1Items) {
				total += item.getAmount();
			}

			assertThat(createdCart).isEqualTo(cart);
			assertThat(createdCart.getItems().size()).isEqualTo(2);
			assertThat(createdCart.getItems()).containsAll(Set.of(item1, item2));
			assertThat(createdCart.getUser()).isEqualTo(user);
			assertThat(createdCart.getTotal()).isEqualTo(total);
		});
	}

	@Test
	@Order(2)
	void testCreateCart_Success2() {
		assertThatNoException().isThrownBy(() -> {
			User user = UserMockData.userList.get(1);
			Product product1 = ProductMockData.products.get(0);
			Product product2 = ProductMockData.products.get(1);
			CartItem item1 = new CartItem(product1, 3);
			CartItem item2 = new CartItem(product2, 4);

			Cart cart = new Cart();

			cart1Items.addAll(Set.of(item1, item2));
			cart.addItems(cart1Items);
			cart.setUser(user);
			this.carts.add(cart);

			Cart createdCart = cartService.createCart(cart);
			double total = 0.;
			for (CartItem item: this.cart1Items) {
				total += item.getAmount();
			}

			assertThat(createdCart).isEqualTo(cart);
			assertThat(createdCart.getItems().size()).isEqualTo(2);
			assertThat(createdCart.getItems()).containsAll(Set.of(item1, item2));
			assertThat(createdCart.getUser()).isEqualTo(user);
			assertThat(createdCart.getTotal()).isEqualTo(total);
		});
	}

	@AfterAll
	void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
			cartService.deleteMany(this.carts);
		});
		assertThatNoException().isThrownBy(() -> {
			productService.deleteMany(ProductMockData.products);
		});
		assertThatNoException().isThrownBy(() -> {
			userService.deleteMany(UserMockData.userList);
		});
	}
}
