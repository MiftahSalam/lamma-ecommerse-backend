package com.miftah.lamaecommerse.models.product;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miftah.lamaecommerse.models.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Table(name = "product_sizes", schema = "public")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
public class ProductSize extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1043188378074685073L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@NonNull
	private String name;

	@ManyToMany(mappedBy = "sizes", cascade = { CascadeType.REMOVE, CascadeType.REFRESH,
			CascadeType.MERGE }, fetch = FetchType.EAGER)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	private Set<Product> products = new HashSet<>();

	@Override
	@PostConstruct
	protected void initSuperId() {
		super.setBaseId(this.id);
	}

}
