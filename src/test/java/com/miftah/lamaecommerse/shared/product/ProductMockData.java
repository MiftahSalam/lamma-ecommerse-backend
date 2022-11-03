package com.miftah.lamaecommerse.shared.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.miftah.lamaecommerse.models.product.Product;

public class ProductMockData {
	public static List<Product> products = new ArrayList<Product>();

	static {
		Product samsunga32 = new Product("Samsung A32", "Android smartphone from samsung", true, (float) 2_500_000);
		Product chair = new Product("Chair", "Cute chair for work", true, (float) 500_000);
		Product buavita = new Product("Buavita Jeruk", "Fresh orange juice", false, (float) 5000);

//		samsunga32.setCategories(Set.of(CategoryMockData.categoryList.get(0)));
//		samsunga32.setSizes(Set.of(ProductSizeMockData.productSizes.get(0)));
//		chair.setCategories(Set.of(CategoryMockData.categoryList.get(1), CategoryMockData.categoryList.get(3)));
//		chair.setSizes(Set.of(ProductSizeMockData.productSizes.get(1)));
//		buavita.setCategories(Set.of(CategoryMockData.categoryList.get(2)));

		products.addAll(Set.of(samsunga32, chair, buavita));
	}

}
