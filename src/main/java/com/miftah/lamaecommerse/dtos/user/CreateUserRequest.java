package com.miftah.lamaecommerse.dtos.user;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.miftah.lamaecommerse.models.User;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

	public CreateUserRequest() {
	}

	public CreateUserRequest(String fullname, String username, String email, String password, String imgUrl) {
		this.fullname = fullname;
		this.username = username;
		this.password = password;
		this.email = email;
		this.imgUrl = imgUrl;
	}

	@NonNull
	@NotEmpty(message = "Fullname is required")
	protected String fullname;

	@NonNull
	@NotEmpty(message = "Username is required")
	protected String username;

	@NonNull
	@NotEmpty(message = "Email is required")
	@Email
	protected String email;

	@NonNull
	@NotEmpty(message = "Password is required")
	protected String password;

	protected String imgUrl;

	public User toUser() {
		return new User(this.fullname, this.username, this.email, this.password);
	}

	public static CreateUserRequest fromUser(User user) {
		return new CreateUserRequest(user.getFullname(), user.getUsername(), user.getEmail(), user.getPassword(),
				user.getImgUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, fullname, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateUserRequest other = (CreateUserRequest) obj;
		return Objects.equals(email, other.email) && Objects.equals(fullname, other.fullname)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "CreateUserRequest [fullname=" + fullname + ", username=" + username + ", email=" + email + "]";
	}

}
