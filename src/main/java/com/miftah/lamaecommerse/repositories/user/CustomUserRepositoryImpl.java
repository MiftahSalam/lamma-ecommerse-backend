package com.miftah.lamaecommerse.repositories.user;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.miftah.lamaecommerse.models.User;

public class CustomUserRepositoryImpl implements CustomUserRepository {

	@Autowired
	EntityManager entityManager;

	@Override
	public User findByUsername(String username) {
		Query query = entityManager.createNativeQuery("SELECT * FROM users WHERE username = ?", User.class);
		query.setParameter(1, username);

		User user = (User) query.getSingleResult();

		return user;
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
