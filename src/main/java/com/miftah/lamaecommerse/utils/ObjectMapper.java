package com.miftah.lamaecommerse.utils;

import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.models.User;

public class ObjectMapper {
	public static UserView userToUserView(User user, String token) {
		return new UserView(user.getId(), token, user.getFullname(), user.getUsername(), user.getEmail(),
				user.isAdmin(), user.getImgUrl(), user.getCreatedAt(), user.getUpdatedAt());
	}

	public static User userViewToUser(UserView user) {
		User newUser = new User();

		newUser.setId(user.id());
		newUser.setFullname(user.fullname());
		newUser.setUsername(user.username());
		newUser.setEmail(user.email());
		newUser.setId(user.id());

		return newUser;
	}
}
