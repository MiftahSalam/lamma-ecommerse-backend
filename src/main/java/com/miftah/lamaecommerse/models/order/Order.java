package com.miftah.lamaecommerse.models.order;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.miftah.lamaecommerse.models.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

//@Entity
//@Table(name = "orders", schema = "public")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class Order implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7661816777090348381L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Column(name = "date", nullable = false)
	@NotNull
	private Date orderDate;

//	@Column(name = "order_code", nullable = false)
	@NotNull
	private String orderCode;

//	@Column(name = "amount", nullable = false)
	@NotNull
	private double amount;

//	@Column(name = "status", nullable = false)
	@NotNull
	private String status;

//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
//	@JoinColumn(name = "id")
	private User customer;

//	@Column(name = "customer_name", length = 255, nullable = false)
//	@NotNull
//	private String customerName;
//
//	@Column(name = "customer_address", length = 255, nullable = false)
//	@NotNull
//	private String customerAddress;
//
//	@Column(name = "customer_email", length = 128, nullable = false)
//	@NotNull
//	private String customerEmail;
//
//	@Column(name = "customer_phone", length = 128, nullable = false)
//	@NotNull
//	private String customerPhone;
}
