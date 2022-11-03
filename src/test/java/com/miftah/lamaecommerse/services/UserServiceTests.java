package com.miftah.lamaecommerse.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;
import com.miftah.lamaecommerse.shared.user.UserMockData;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceTests {
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
	void testCreateUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (User user : UserMockData.userList) {
				log.info("create user {}", user.getUsername());

				User createdUser = userService.create(user);
				assertThat(createdUser.getFullname()).isEqualTo(user.getFullname());

				BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
				assertThat(bCryptPasswordEncoder.matches(UserMockData.passwordList.get(i), createdUser.getPassword()))
						.isEqualTo(true);

				user.setBaseId(user.getId());
				UserMockData.userList.set(i, user);
				User userUpdate = UserMockData.userListUpdate.get(i);
				userUpdate.setId(user.getId());
				userUpdate.setBaseId(user.getId());
				UserMockData.userListUpdate.set(i, userUpdate);
				i++;
			}
		});
	}

	@Test
	@Order(2)
	void testCreateUser_Error_UserAlreadyExist() {
		assertThatExceptionOfType(AlreadyExistException.class).isThrownBy(() -> {
			User createdUser = userService.create(UserMockData.userList.get(0));
			assertThat(createdUser.getFullname()).isEqualTo(UserMockData.userList.get(0).getFullname());
		});
	}

	@Test
	@Order(3)
	void testGetOneUserById_Success() {
		assertThatNoException().isThrownBy(() -> {
			User userMiftah = userService.getById(UserMockData.userList.get(0).getId());
//			User userMiftah = userService.getUserById(UserMockData.userList.get(0).getId());
			log.debug("user {}", userMiftah);
			log.debug("user 0 {}", UserMockData.userList.get(0));
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(4)
	void testGetOneUserById_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			User userMiftah = userService.getById((long) 2_000_000);
//			User userMiftah = userService.getUserById((long) 2_000_000);
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(5)
	void testGetOneUserById_NullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> {
			User userMiftah = userService.getById(null);
//			User userMiftah = userService.getUserById(null);
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(6)
	void testGetOneUserByUsername_Success() {
		assertThatNoException().isThrownBy(() -> {
			User userMiftah = userService.getUserByUsername(UserMockData.userList.get(0).getUsername());
			log.info("user {}", userMiftah);
			log.info("user 0 {}", UserMockData.userList.get(0));
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(7)
	void testGetOneUserByUsername_NotFound() {
		assertThatExceptionOfType(EmptyResultDataAccessException.class).isThrownBy(() -> {
			User userMiftah = userService.getUserByUsername("someone");
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(8)
	void testGetOneUserByUsername_NullId() {
		assertThatExceptionOfType(InvalidDataAccessResourceUsageException.class).isThrownBy(() -> {
			User userMiftah = userService.getUserByUsername(null);
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(9)
	void testUpdateUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (User user : UserMockData.userListUpdate) {
				log.info("update user {}", user.getUsername());

				User updatedUser = userService.update(user);
				UserMockData.userListUpdate.set(i, updatedUser);
				i++;
				user.setPassword(updatedUser.getPassword());

				assertThat(updatedUser).isEqualTo(user);
			}
		});
	}

	@Test
	@Order(10)
	void testUpdateUser_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			User updateUser = new User("Miftah Salam", "mifsfssdftah-salam", "salam.mifsdfftah@gmail.com",
					UserMockData.passwordList.get(0));
			updateUser.setId((long) 2_000_000);
			updateUser.setBaseId((long) 2_000_000);
			User userMiftah = userService.update(updateUser);
			assertEquals(userMiftah, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(11)
	void testUpdateUser_NullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> {
			User user = userService.update(new User());
			assertEquals(user, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(12)
	void testDeleteUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			User userToDelete = UserMockData.userListUpdate.get(0);
			User user = userService.delete(userToDelete);
//			User user = userService.deleteUser(UserMockData.userListUpdate.get(0));
			assertEquals(user, UserMockData.userListUpdate.get(0));

		});
	}

	@Test
	@Order(13)
	void testDeleteUser_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			User userToDelete = new User("Miftah Salam", "mifsfssdftah-salam", "salam.mifsdfftah@gmail.com",
					UserMockData.passwordList.get(0));
			userToDelete.setId((long) 2_000_000);
			userToDelete.setBaseId((long) 2_000_000);
			User deletedUser = userService.delete(userToDelete);
//			User deletedUser = userService.deleteUser(userToDelete);
			assertEquals(deletedUser, UserMockData.userList.get(0));
		});
	}

	@Test
	@Order(14)
	void testDeleteUser_NullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> {
			User user = userService.delete(new User());
//			User user = userService.deleteUser(new User());
			assertEquals(user, UserMockData.userList.get(0));
		});
	}

	@AfterAll
	static void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
//			userServiceStatic.deleteUsers(UserMockData.userList);
//			userServiceStatic.deleteUsers(UserMockData.userListUpdate);
			userServiceStatic.deleteMany(UserMockData.userList);
			userServiceStatic.deleteMany(UserMockData.userListUpdate);
		});
	}

}
