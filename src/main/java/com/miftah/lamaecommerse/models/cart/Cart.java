package com.miftah.lamaecommerse.models.cart;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.miftah.lamaecommerse.models.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "carts", schema = "public")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Cart implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -6838442675661295497L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = CartItem.class, cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<CartItem> items = new HashSet<>();

	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
//	@NonNull
	private User user;

	@Column(name = "total", nullable = false)
	@NotNull
	private double total;

	@Transient
	public void addItems(Set<CartItem> cartItems) {
		items.addAll(cartItems);
		for (CartItem cartItem : cartItems) {
			cartItem.calculateAmount();
			cartItem.setCart(this);
		}
		calculateTotal();
	}

	@Transient()
	@PreUpdate()
	@PrePersist
	void calculateTotal() {
		this.total = 0.;
		for (CartItem item: this.items) {
			this.total += item.getAmount();
		}
	}

}
