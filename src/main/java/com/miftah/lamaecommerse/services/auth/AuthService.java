package com.miftah.lamaecommerse.services.auth;

import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UserLogin;
import com.miftah.lamaecommerse.dtos.user.UserView;

public interface AuthService {
	public UserView register(CreateUserRequest createUserRequest) throws Exception;

	public UserView login(UserLogin user) throws Exception;
}
