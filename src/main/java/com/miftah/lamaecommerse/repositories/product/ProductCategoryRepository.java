package com.miftah.lamaecommerse.repositories.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miftah.lamaecommerse.models.product.ProductCategory;

@Repository
public interface ProductCategoryRepository extends CustomProductRepository<ProductCategory> {
	@Query(value = "SELECT * FROM product_categories WHERE name = :NAME", nativeQuery = true)
	public ProductCategory findOneByName(@Param("NAME") String name);
}
