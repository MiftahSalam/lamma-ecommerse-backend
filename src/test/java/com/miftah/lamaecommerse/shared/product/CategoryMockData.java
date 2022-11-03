package com.miftah.lamaecommerse.shared.product;

import java.util.ArrayList;
import java.util.List;

import com.miftah.lamaecommerse.models.product.ProductCategory;

public class CategoryMockData {
	public static List<ProductCategory> categoryList = new ArrayList<ProductCategory>();

	static {
		categoryList.addAll(List.of(new ProductCategory("Electronic"), new ProductCategory("Furniture"),
				new ProductCategory("Food"), new ProductCategory("Property")));
	}

}
