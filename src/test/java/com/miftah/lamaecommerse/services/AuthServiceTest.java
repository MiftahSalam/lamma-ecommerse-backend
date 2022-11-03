package com.miftah.lamaecommerse.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UserLogin;
import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.services.auth.AuthService;
//import com.miftah.lamaecommerse.services.user.UserService;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;
import com.miftah.lamaecommerse.shared.user.UserMockData;
import com.miftah.lamaecommerse.utils.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class AuthServiceTest {
	@Autowired
	private AuthService authService;

	@Autowired
	private UserServiceImpl userService;
//	private UserService userService;

	private static UserServiceImpl userServiceStatic;
//	private static UserService userServiceStatic;

	@PostConstruct
	public void init() {
		userServiceStatic = userService;
	}

	@Test
	@Order(1)
	void testRegisterUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (User user : UserMockData.userList) {
				log.info("register user {}", user.getUsername());

				CreateUserRequest userRequest = CreateUserRequest.fromUser(user);
				UserView createdUser = authService.register(userRequest);
				assertThat(createdUser.fullname()).isEqualTo(user.getFullname());

				UserMockData.userList.set(i, ObjectMapper.userViewToUser(createdUser));
				User userUpdate = UserMockData.userListUpdate.get(i);
				userUpdate.setId(user.getId());
				UserMockData.userListUpdate.set(i, userUpdate);
				i++;
			}
		});
	}

//	@Disabled
	@Test
	@Order(2)
	void testRegisterUser_AlreadyExist() {
		User user = UserMockData.userList.get(0);
		log.info("register user {}", user.getUsername());

		CreateUserRequest userRequest = CreateUserRequest.fromUser(user);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			UserView createdUserView = authService.register(userRequest);
			User createdUser = ObjectMapper.userViewToUser(createdUserView);
			assertNull(createdUser);
		});
	}

	@Test
	@Order(3)
	void testLoginUser_Success() {
		User user = UserMockData.userList.get(1);
		user.setPassword(UserMockData.passwordList.get(1));
		log.info("login user {}", user.getUsername());

		UserLogin userRequest = new UserLogin(user.getUsername(), user.getPassword());
		assertThatNoException().isThrownBy(() -> {
			UserView loggedUser = authService.login(userRequest);
			assertThat(loggedUser.fullname()).isEqualTo(user.getFullname());
		});
	}

	@Test
	@Order(4)
	void testLoginUser_BadCredential() {
		User user = UserMockData.userList.get(1);
		log.info("login user {}", user.getUsername());

		UserLogin userRequest = new UserLogin(user.getUsername(), "afgdfg");
		assertThatExceptionOfType(BadCredentialsException.class).isThrownBy(() -> {
			UserView loggedUser = authService.login(userRequest);
			assertThat(loggedUser.fullname()).isEqualTo(user.getFullname());
		});
	}

	@AfterAll
	static void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
			userServiceStatic.deleteMany(UserMockData.userList);
			userServiceStatic.deleteMany(UserMockData.userListUpdate);
//			userServiceStatic.deleteUsers(UserMockData.userList);
//			userServiceStatic.deleteUsers(UserMockData.userListUpdate);
		});
	}

}
