package com.miftah.lamaecommerse.controllers.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miftah.lamaecommerse.controllers.AbstractControllerTest;
import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UpdateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.services.auth.JwtService;
//import com.miftah.lamaecommerse.services.user.UserService;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;
import com.miftah.lamaecommerse.shared.user.UserMockData;

import lombok.extern.slf4j.Slf4j;

@TestMethodOrder(OrderAnnotation.class)
@Slf4j
//@WithMockUser(roles = { Role.USER_ROLE, Role.ADMIN_ROLE })
public class UserControllerTest extends AbstractControllerTest {
	@Autowired
	UserServiceImpl userService;
//	UserService userService;

	@Autowired
	JwtService jwtService;

	private static UserServiceImpl userServiceStatic;
//	private static UserService userServiceStatic;
	private String uri = "/user";
	private String token;

	@PostConstruct
	public void init() {
		userServiceStatic = userService;
	}

	@Override
	@BeforeEach
	public void setup() {
		super.setup();
		UserView userLogin;
		try {
			userLogin = jwtService.generateToken("user1", "123456");
			this.token = userLogin.token();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(1)
	void testCreateUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (User user : UserMockData.userList) {
				CreateUserRequest userRequest = CreateUserRequest.fromUser(user);

				String user1StrJson = super.mapToJson(userRequest);
				MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + this.token)
								.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
						.andReturn();

				int status = mvcResult.getResponse().getStatus();
				String content = mvcResult.getResponse().getContentAsString();
				TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
				};
				Gson gson = new Gson();
				BaseResponse<User> createdUserResp = gson.fromJson(content, typeToken.getType());
				User createdUser = createdUserResp.getData();
				CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(createdUser);

				UserMockData.userList.set(i, createdUser);
				User userUpdate = UserMockData.userListUpdate.get(i);
				userUpdate.setId(createdUser.getId());
				UserMockData.userListUpdate.set(i, userUpdate);

				log.info("content {}", content);
				i++;

				assertEquals(201, status);
				assertThat(userRequestCreated).isEqualTo(userRequest);
				assertNull(userRequestCreated.getPassword());
			}
		});
	}

	@Test
	@Order(2)
	void testCreateUser_AlreadyExist() throws Exception {
		for (User user : UserMockData.userList) {
			CreateUserRequest userRequest = CreateUserRequest.fromUser(user);

			String user1StrJson = super.mapToJson(userRequest);
			MvcResult mvcResult = mvc
					.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + this.token)
							.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();
			TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
			};
			Gson gson = new Gson();
			BaseResponse<User> createdUserResp = gson.fromJson(content, typeToken.getType());

			log.info("content {}", content);

			assertEquals(400, status);
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				User createdUser = createdUserResp.getData();
				assertNull(createdUser);
			});
		}
	}

//	@Disabled
	@Test()
	@Order(3)
	void testGetOneUser_Success() throws Exception {
		String uri = this.uri + "/" + UserMockData.userList.get(0).getId();
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.header("Authorization", "Bearer " + this.token).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> userResp = gson.fromJson(content, typeToken.getType());
		User getUser = userResp.getData();
		CreateUserRequest userRequest = CreateUserRequest.fromUser(UserMockData.userList.get(0));
		CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(getUser);

		log.info("content {}", content);

		assertEquals(200, status);
		assertThat(userRequestCreated).isEqualTo(userRequest);
		assertNull(userRequestCreated.getPassword());
	}

	@Test()
	@Order(4)
	void testGetOneUser_NotFound() throws Exception {
		String uri = this.uri + "/2000000";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.header("Authorization", "Bearer " + this.token).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> userResp = gson.fromJson(content, typeToken.getType());

		log.info("content {}", content);

		assertEquals(404, status);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			User getUser = userResp.getData();
			CreateUserRequest userRequest = CreateUserRequest.fromUser(UserMockData.userList.get(0));
			CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(getUser);

			assertThat(userRequestCreated).isEqualTo(userRequest);
		});

	}

	@Test()
	@Order(5)
	void testGetOneUser_BadRequestInvalidParamId() throws Exception {
		String uri = this.uri + "/sdfs";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> userResp = gson.fromJson(content, typeToken.getType());

		log.info("content {}", content);

		assertEquals(400, status);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			User getUser = userResp.getData();
			CreateUserRequest userRequest = CreateUserRequest.fromUser(UserMockData.userList.get(0));
			CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(getUser);

			assertThat(userRequestCreated).isEqualTo(userRequest);
		});

	}

	@Test
	@Order(6)
	void testUpdateUser_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (User user : UserMockData.userListUpdate) {
				UpdateUserRequest userRequest = UpdateUserRequest.fromUser(user);

				String user1StrJson = super.mapToJson(userRequest);
				MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.put(this.uri).header("Authorization", "Bearer " + this.token)
								.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
						.andReturn();

				int status = mvcResult.getResponse().getStatus();
				String content = mvcResult.getResponse().getContentAsString();
				TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
				};
				Gson gson = new Gson();
				BaseResponse<User> updatedUserResp = gson.fromJson(content, typeToken.getType());
				User updatedUser = updatedUserResp.getData();
				UpdateUserRequest userRequestUpdated = UpdateUserRequest.fromUser(updatedUser);
				UserMockData.userListUpdate.set(i, updatedUser);

				log.info("content {}", content);
				i++;

				assertEquals(200, status);
				assertThat(userRequestUpdated).isEqualTo(userRequest);
				assertNull(userRequestUpdated.getPassword());
			}
		});
	}

	@Test
	@Order(7)
	void testUpdateUser_UserNotFound() throws Exception {
		User user = new User("Miftah Salam", "mifsfssdftah-salam", "salam.mifsdfftah@gmail.com",
				UserMockData.passwordList.get(0));
		user.setId((long) 200_000);
		UpdateUserRequest userRequest = UpdateUserRequest.fromUser(user);

		String user1StrJson = super.mapToJson(userRequest);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(this.uri).header("Authorization", "Bearer " + this.token)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> updatedUserResp = gson.fromJson(content, typeToken.getType());

		log.info("content {}", content);

		assertEquals(404, status);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			User updatedUser = updatedUserResp.getData();
			CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(updatedUser);

			assertThat(userRequestCreated).isEqualTo(userRequest);
			assertThat(userRequestCreated).isEqualTo(userRequest);
		});

	}

	@Test
	@Order(8)
	void testUpdateUser_NullId() throws Exception {
		UpdateUserRequest userRequest = new UpdateUserRequest();

		String user1StrJson = super.mapToJson(userRequest);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(this.uri).header("Authorization", "Bearer " + this.token)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> updatedUserResp = gson.fromJson(content, typeToken.getType());

		log.info("content {}", content);

		assertEquals(400, status);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			User updatedUser = updatedUserResp.getData();
			CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(updatedUser);

			assertThat(userRequestCreated).isEqualTo(userRequest);
			assertThat(userRequestCreated).isEqualTo(userRequest);
		});

	}

	@Test
	@Order(9)
	void testDeleteUser_Success() throws Exception {
		String uri = this.uri + "/" + UserMockData.userList.get(0).getId();

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).header("Authorization", "Bearer " + this.token))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> deletedUserResp = gson.fromJson(content, typeToken.getType());
		User deletedUser = deletedUserResp.getData();

		log.info("content {}", content);

		assertEquals(200, status);
		assertThat(UserMockData.userListUpdate.get(0)).isEqualTo(deletedUser);
		assertNull(deletedUser.getPassword());
	}

	@Test
	@Order(10)
	void testDeleteUser_NotFound() throws Exception {
		String uri = this.uri + "/" + UserMockData.userList.get(0).getId();

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).header("Authorization", "Bearer " + this.token))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> deletedUserResp = gson.fromJson(content, typeToken.getType());
		User deletedUser = deletedUserResp.getData();

		log.info("content {}", content);

		assertEquals(404, status);
		assertNull(deletedUser);
	}

	@Test
	@Order(11)
	void testUpdateUser_InvalidEmailFormat() throws Exception {
		User user = new User("Miftah Salam", "", "salam.mifsdfftah", UserMockData.passwordList.get(0));
		user.setId(UserMockData.userList.get(0).getId());
		UpdateUserRequest userRequest = UpdateUserRequest.fromUser(user);

		String user1StrJson = super.mapToJson(userRequest);
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put(this.uri).header("Authorization", "Bearer " + this.token)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(user1StrJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		TypeToken<BaseResponse<User>> typeToken = new TypeToken<BaseResponse<User>>() {
		};
		Gson gson = new Gson();
		BaseResponse<User> updatedUserResp = gson.fromJson(content, typeToken.getType());

		log.info("content {}", content);

		assertEquals(400, status);
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			User updatedUser = updatedUserResp.getData();
			CreateUserRequest userRequestCreated = CreateUserRequest.fromUser(updatedUser);

			assertThat(userRequestCreated).isEqualTo(userRequest);
			assertThat(userRequestCreated).isEqualTo(userRequest);
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
