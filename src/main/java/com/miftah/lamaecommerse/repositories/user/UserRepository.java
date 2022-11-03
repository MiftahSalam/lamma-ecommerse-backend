package com.miftah.lamaecommerse.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miftah.lamaecommerse.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
}
