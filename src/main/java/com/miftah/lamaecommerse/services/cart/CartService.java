package com.miftah.lamaecommerse.services.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.InternalErrorException;
import com.miftah.lamaecommerse.models.User;
import com.miftah.lamaecommerse.models.cart.Cart;
import com.miftah.lamaecommerse.models.cart.CartItem;
import com.miftah.lamaecommerse.repositories.cart.CartItemRepository;
import com.miftah.lamaecommerse.repositories.cart.CartRepository;
import com.miftah.lamaecommerse.repositories.user.UserRepository;
import com.miftah.lamaecommerse.services.BaseServiceImpl;

@Service
public class CartService extends BaseServiceImpl<Cart, CartRepository> {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	public Cart createCart(Cart cart) throws Exception {
		Cart gettedCart;
		try {
			gettedCart = getById(cart.getId());
			gettedCart.addItems(cart.getItems());
		} catch (InvalidDataAccessApiUsageException e) {
			gettedCart = createNewCart(cart);
		} catch (AlreadyExistException e) {
			System.out.println("already exist");
			gettedCart = cart;
		} catch (Exception e) {
			gettedCart = createNewCart(cart);
		}

		return gettedCart;
	}

	@Override
	public void deleteMany(List<Cart> entities) throws BaseException {
		try {
			for (Cart cart : entities) {
				for (CartItem item : cart.getItems()) {
					cart.getItems().remove(item);
					cartItemRepository.delete(item);
				}
			}
			cartRepository.deleteAllInBatch(entities);
		} catch (Exception e) {
			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
		}
	}
	
	private Cart createNewCart(Cart cart) throws Exception {
		Optional<User> user = userRepository.findById(cart.getUser().getId());
		cart.setUser(user.get());

		for (CartItem item : cart.getItems()) {
			item.setCart(cart);
			cartItemRepository.save(item);
		}

		Cart newCart = cartRepository.save(cart);


		return newCart;
	}
}
