package com.miftah.lamaecommerse.repositories.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miftah.lamaecommerse.models.product.ProductSize;

@Repository
public interface ProductSizeRepository extends CustomProductRepository<ProductSize> {
	@Query(value = "SELECT * FROM product_sizes WHERE name = :NAME", nativeQuery = true)
	public ProductSize findOneByName(@Param("NAME") String name);
}
