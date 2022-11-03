package com.miftah.lamaecommerse.repositories.user;

import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.models.User;

public interface CustomUserRepository {
	public User findByUsername(String username) throws BaseException;

	public User findByEmail(String email) throws BaseException;

}
