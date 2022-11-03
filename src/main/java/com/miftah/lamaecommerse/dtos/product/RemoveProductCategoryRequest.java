package com.miftah.lamaecommerse.dtos.product;

import javax.validation.constraints.NotNull;

public record RemoveProductCategoryRequest(@NotNull long categoryId) {

}
