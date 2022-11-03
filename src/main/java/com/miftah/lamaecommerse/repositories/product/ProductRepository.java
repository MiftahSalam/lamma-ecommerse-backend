package com.miftah.lamaecommerse.repositories.product;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miftah.lamaecommerse.models.product.Product;

@Repository
public interface ProductRepository extends CustomProductRepository<Product> {
	@Query(value = "SELECT * FROM products WHERE name = :NAME", nativeQuery = true)
	public Product findOneByName(@Param("NAME") String name);

//	@Query(value = "SELECT p FROM products p join p.categories c WHERE c.name = :CATEGORY", nativeQuery = false)
	@Query(value = "SELECT * FROM products join products_categories on products.id = products_categories.products_id join product_categories on products_categories.categories_id = product_categories.id where product_categories.name = :CATEGORY", nativeQuery = true)
	public List<Product> findByCategoryName(@Param("CATEGORY") String name);
}
