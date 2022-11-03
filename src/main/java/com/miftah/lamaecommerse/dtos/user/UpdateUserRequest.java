package com.miftah.lamaecommerse.dtos.user;

import java.util.Date;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.miftah.lamaecommerse.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest extends CreateUserRequest {
	@NotNull(message = "Id is required")
	@Digits(fraction = 0, integer = 2 ^ 32, message = "Id must be digit")
	@Min(value = 1, message = "Id must be greater than 0")
	protected long id;

	protected boolean isAdmin;

	protected Date updatedAt;

	public UpdateUserRequest(String fullname, String username, String email, String password, String imgUrl) {
		super(fullname, username, email, password, imgUrl);
	}

	@PostConstruct()
	void updateDate() {
		this.updatedAt = new Date();
	}

	public User toUser() {
		User user = new User(this.fullname, this.username, this.email, this.password);

		user.setId(this.id);
		user.setAdmin(this.isAdmin);
		user.setUpdatedAt(new Date());
		user.setImgUrl(this.imgUrl);

		return user;
	}

	public static UpdateUserRequest fromUser(User user) {
		UpdateUserRequest request = new UpdateUserRequest(user.getFullname(), user.getUsername(), user.getEmail(),
				user.getPassword(), user.getImgUrl());

		request.setId(user.getId());
		request.setAdmin(user.isAdmin());
		request.setImgUrl(user.getImgUrl());
		request.setUpdatedAt(user.getUpdatedAt());

		return request;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, isAdmin, updatedAt);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;

		UpdateUserRequest other = (UpdateUserRequest) obj;
		return id == other.id && isAdmin == other.isAdmin && Objects.equals(email, other.email)
				&& Objects.equals(fullname, other.fullname) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UpdateUserRequest [id=" + id + ", isAdmin=" + isAdmin + ", updatedAt=" + updatedAt + ", fullname="
				+ fullname + ", username=" + username + ", email=" + email + ", imgUrl=" + imgUrl + "]";
	}

}
