package com.miftah.lamaecommerse.dtos.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateProductRequest(@NotBlank String name, String desc, @NotNull boolean inStock, @NotNull float price,
		String imgUrl) {

}
