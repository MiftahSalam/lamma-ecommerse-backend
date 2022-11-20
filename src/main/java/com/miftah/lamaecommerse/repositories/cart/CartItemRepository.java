package com.miftah.lamaecommerse.repositories.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miftah.lamaecommerse.models.cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
