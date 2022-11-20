package com.miftah.lamaecommerse.models.order;

import java.io.Serializable;

import com.miftah.lamaecommerse.models.product.Product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

//@Entity
//@Table(name = "orders_detail", schema = "public")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class OrderDetail implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 3041323132094849878L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id", nullable = false)
	private Order order;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id", nullable = false)
	private Product product;

//	@Column(name = "quantity", nullable = false)
	private int quantity;

//	@Column(name = "amount", nullable = false)
	private double amount;
}
