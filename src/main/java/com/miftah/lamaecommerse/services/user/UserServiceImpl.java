package com.miftah.lamaecommerse.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.repositories.user.UserRepository;
import com.miftah.lamaecommerse.services.BaseServiceImpl;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserRepository> implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private EntityManager entityManager;
//
//	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Throwable.class })
//	public User create(User user) throws BaseException {
//		try {
//			entityManager.persist(user);
//			return user;
//		} catch (PersistenceException e) {
//			throw new AlreadyExistException(ResponseMessage.ALREADYEXIST, e.getMessage(), "user service");
//		} catch (Exception e) {
//			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
//		}
//	}
//
//	public User getUserById(Long id) throws BaseException {
//		Optional<User> user = userRepository.findById(id);
//		if (user.isEmpty()) {
//			throw new NotFoundException(ResponseMessage.NOTFOUND, "user not found", "user service");
//		} else {
//			return user.get();
//		}
//	}
//
//	@Override
//	public List<User> getAllUsers() throws BaseException {
//		List<User> users = userRepository.findAll();
//		if (users.isEmpty()) {
//			throw new NotFoundException(ResponseMessage.NOTFOUND, "users empty", "user service");
//		} else {
//			return users;
//		}
//	}

//	@Override
//	public void deleteUsers(List<User> users) throws BaseException {
//		try {
//			userRepository.deleteAll(users);
//		} catch (Exception e) {
//			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
//		}
//	}

//	@Override
	public User getUserByUsername(String username) throws BaseException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "user not found", "user service");
		} else {
			return user;
		}
	}

//	@Override
//	public User update(User user) throws BaseException {
//		try {
//			getUserById(user.getId());
//			User updatedUser = userRepository.save(user);
//			return updatedUser;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Override
//	public User deleteUser(User user) throws BaseException {
//		try {
//			User userToDelete = getUserById(user.getId());
//			userRepository.delete(userToDelete);
//			return userToDelete;
//		} catch (Exception e) {
//			throw e;
//		}
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			UserDetails user = getUserByUsername(username);
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException(String.format("User with username - s%, not found", username));
		}
	}
}
