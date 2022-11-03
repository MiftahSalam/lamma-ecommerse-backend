package com.miftah.lamaecommerse.dtos.product;

import javax.validation.constraints.NotNull;

public record GetProductByNameRequest(@NotNull String pName) {

}
