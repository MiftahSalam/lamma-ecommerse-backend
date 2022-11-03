package com.miftah.lamaecommerse.shared.product;

import java.util.ArrayList;
import java.util.List;

import com.miftah.lamaecommerse.models.product.ProductSize;

public class ProductSizeMockData {
	public static List<ProductSize> productSizes = new ArrayList<ProductSize>();

	static {
		productSizes.addAll(List.of(new ProductSize("L"), new ProductSize("M"), new ProductSize("XL")));
	}

}
