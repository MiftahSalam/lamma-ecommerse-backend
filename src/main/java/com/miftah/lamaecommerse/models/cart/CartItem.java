package com.miftah.lamaecommerse.models.cart;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.miftah.lamaecommerse.models.product.Product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cart_items", schema = "public")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7135854747458744183L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "product_cart_item_pk"))
	@NonNull
	@NotNull
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Cart.class, cascade = {CascadeType.ALL})
//	@JoinColumn(name = "cart_item_id", referencedColumnName = "id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Cart cart;

	@Column(name = "quantity", nullable = false)
	@NonNull
	private Integer quantity;

	@Column(name = "amount", nullable = false)
	@NotNull
	private double amount;

}
