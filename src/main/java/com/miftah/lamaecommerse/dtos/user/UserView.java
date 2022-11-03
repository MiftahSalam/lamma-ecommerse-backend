package com.miftah.lamaecommerse.dtos.user;

import java.util.Date;

public record UserView(long id, String token, String fullname, String username, String email, boolean isAdmin,
		String imgUrl, Date createdAt, Date updatedAt) {

}
