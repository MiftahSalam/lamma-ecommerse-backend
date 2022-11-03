package com.miftah.lamaecommerse.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UserLogin;
import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.InternalErrorException;
import com.miftah.lamaecommerse.services.auth.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping()
	ResponseEntity<BaseResponse<UserView>> register(@RequestBody @Valid CreateUserRequest request) throws Exception {
		BaseResponse<UserView> baseResponse;

		try {
			UserView createdUser = authService.register(request);
			baseResponse = new BaseResponse<UserView>(HttpStatus.CREATED, "201", "Success", createdUser);
		} catch (AlreadyExistException e) {
			baseResponse = new BaseResponse<UserView>(HttpStatus.BAD_REQUEST, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "user service");
			baseResponse = new BaseResponse<UserView>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<UserView>> login(@RequestBody @Valid UserLogin request) throws Exception {
		BaseResponse<UserView> baseResponse;

		try {
			UserView loggedUser = authService.login(request);
			baseResponse = new BaseResponse<UserView>(HttpStatus.OK, "200", "Success", loggedUser);
		} catch (BadCredentialsException e) {
			baseResponse = new BaseResponse<UserView>(HttpStatus.UNAUTHORIZED, "401", e.getMessage(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "user service");
			baseResponse = new BaseResponse<UserView>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}
}
