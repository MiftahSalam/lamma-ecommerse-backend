package com.miftah.lamaecommerse.models.product;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.miftah.lamaecommerse.models.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "products", schema = "public")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
public class Product extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6106982799977494554L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@NonNull
	private String name;

	@Column(nullable = false)
	@NonNull
	private String description;

	@Column(nullable = false)
	@NonNull
	private Boolean inStock;

	@Column(nullable = false)
	@NonNull
	private Float price;

	@Column()
	private String imageUrl;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "products_categories", joinColumns = {
			@JoinColumn(referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName = "id") })
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<ProductCategory> categories = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "products_sizes", joinColumns = {
			@JoinColumn(referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName = "id") })
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<ProductSize> sizes = new HashSet<>();

	@Override
	@PostConstruct
	protected void initSuperId() {
		super.setBaseId(getBaseId());

	}

	public void addCategory(ProductCategory category) {
		this.categories.add(category);
		category.getProducts().add(this);
	}

	public void addSize(ProductSize size) {
		this.sizes.add(size);
		size.getProducts().add(this);
	}

	public void removeCategory(ProductCategory category) {
		if (this.categories.remove(category)) {
			category.getProducts().remove(this);
		}
	}

	public void removeSize(ProductSize productSize) {
		if (this.sizes.remove(productSize)) {
			productSize.getProducts().remove(this);
		}
	}
}
