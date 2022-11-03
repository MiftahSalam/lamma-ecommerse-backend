package com.miftah.lamaecommerse.shared.user;

import java.util.ArrayList;
import java.util.List;

import com.miftah.lamaecommerse.models.User;

public class UserMockData {
	public static List<User> userList = new ArrayList<User>();
	public static List<User> userListUpdate = new ArrayList<User>();
	public static List<String> passwordList = new ArrayList<String>();

	static {
		passwordList.add("123456");
		passwordList.add("12345678");

		User new_user = new User("Miftah Salam", "miftah-salam", "salam.miftah@gmail.com", passwordList.get(0));
		User new_user1 = new User("Erwanzi Pranadipa", "erzi", "erzi@gmail.com", passwordList.get(1));
		User user_update = new User("Miftah", "mifta-alam", "salam.miftah@yahoo.com", passwordList.get(0));
		User user_update1 = new User("Erwanzi Pranadipa", "erziwanzi", "erzi@gmail.com", passwordList.get(1));

		userListUpdate.add(user_update);
		userListUpdate.add(user_update1);
		userList.add(new_user);
		userList.add(new_user1);
	}

}
