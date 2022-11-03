package com.miftah.lamaecommerse.dtos.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UpdateProductRequest(@NotNull @NotBlank String name, String desc, Boolean inStock, Float price,
		String imgUrl) {

}
