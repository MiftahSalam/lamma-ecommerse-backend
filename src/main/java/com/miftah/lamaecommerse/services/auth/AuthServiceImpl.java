package com.miftah.lamaecommerse.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UserLogin;
import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
//	private UserService userService;
	private UserServiceImpl userService;

	@Autowired
	private JwtService jwtService;

	@Override
	public UserView register(CreateUserRequest createUserRequest) throws Exception {
		userService.create(createUserRequest.toUser());
		var userView = jwtService.generateToken(createUserRequest.getUsername(), createUserRequest.getPassword());

		return userView;
	}

	@Override
	public UserView login(UserLogin user) throws Exception {
		return jwtService.generateToken(user.username(), user.password());
	}

}
