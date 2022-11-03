package com.miftah.lamaecommerse.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.dtos.user.CreateUserRequest;
import com.miftah.lamaecommerse.dtos.user.UpdateUserRequest;
import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.BadRequestException;
import com.miftah.lamaecommerse.exceptions.InternalErrorException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.services.user.UserServiceImpl;

@RestController()
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserServiceImpl userService;
//	UserService userService;

	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
//	@RolesAllowed(value = Role.USER_ROLE)
	ResponseEntity<BaseResponse<User>> createUser(@RequestBody @Valid CreateUserRequest user)
			throws JsonProcessingException {
		BaseResponse<User> baseResponse;

		try {
			User createdUser = userService.create(user.toUser());
			baseResponse = new BaseResponse<User>(HttpStatus.CREATED, "201", "Success", createdUser);
		} catch (AlreadyExistException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.BAD_REQUEST, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "user service");
			baseResponse = new BaseResponse<User>(HttpStatus.INTERNAL_SERVER_ERROR, "500", exp.getRespMsg().toString(),
					null);
		}

		return baseResponse.buildResponse();
	}

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BaseResponse<List<User>>> getAllUsers() throws JsonProcessingException {
		BaseResponse<List<User>> baseResponse;

		try {
			List<User> users = userService.getAll();
//			List<User> users = userService.getAllUsers();
			baseResponse = new BaseResponse<List<User>>(HttpStatus.OK, "200", "Success", users);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<List<User>>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			baseResponse = new BaseResponse<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					e.getMessage().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<BaseResponse<User>> getUser(@PathVariable(name = "id") long id)
			throws JsonProcessingException {
		BaseResponse<User> baseResponse;

		try {
			User user = userService.getById(id);
//			User user = userService.getUserById(id);
			baseResponse = new BaseResponse<User>(HttpStatus.OK, "200", "Success", user);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (EmptyResultDataAccessException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.NOT_FOUND, "404", e.getMessage(), null);
		} catch (InvalidDataAccessResourceUsageException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.BAD_REQUEST, "400", e.getMessage().toString(), null);
		} catch (InvalidDataAccessApiUsageException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.BAD_REQUEST, "400", e.getMessage().toString(), null);
		} catch (Exception e) {
			baseResponse = new BaseResponse<User>(HttpStatus.INTERNAL_SERVER_ERROR, "500", e.getMessage().toString(),
					null);
		}

		return baseResponse.buildResponse();
	}

	@PutMapping()
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	ResponseEntity<BaseResponse<User>> updateUser(@RequestBody @Valid UpdateUserRequest user)
			throws JsonProcessingException {
		BaseResponse<User> baseResponse;

		try {
			User original_user = user.toUser();
			original_user.setBaseId(original_user.getId());
			User updatedtedUser = userService.update(original_user);
			baseResponse = new BaseResponse<User>(HttpStatus.OK, "200", "Success", updatedtedUser);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (BadRequestException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.BAD_REQUEST, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "user service");
			baseResponse = new BaseResponse<User>(HttpStatus.INTERNAL_SERVER_ERROR, "500", exp.getRespMsg().toString(),
					null);
		}

		return baseResponse.buildResponse();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	ResponseEntity<BaseResponse<User>> deleteUser(@PathVariable("id") long id) throws JsonProcessingException {
		BaseResponse<User> baseResponse;

		try {
			User user = new User();
			user.setId(id);
			user.setBaseId(id);

			User updatedtedUser = userService.delete(user);
//			User updatedtedUser = userService.deleteUser(user);
			baseResponse = new BaseResponse<User>(HttpStatus.OK, "200", "Success", updatedtedUser);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (BadRequestException e) {
			baseResponse = new BaseResponse<User>(HttpStatus.BAD_REQUEST, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "user service");
			baseResponse = new BaseResponse<User>(HttpStatus.INTERNAL_SERVER_ERROR, "500", exp.getRespMsg().toString(),
					null);
		}

		return baseResponse.buildResponse();
	}
}
