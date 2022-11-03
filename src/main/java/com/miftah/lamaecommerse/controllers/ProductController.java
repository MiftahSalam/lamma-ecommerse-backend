package com.miftah.lamaecommerse.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.dtos.product.AddProductCategoryRequest;
import com.miftah.lamaecommerse.dtos.product.AddProductSizeRequest;
import com.miftah.lamaecommerse.dtos.product.CreateProductRequest;
import com.miftah.lamaecommerse.dtos.product.RemoveProductCategoryRequest;
import com.miftah.lamaecommerse.dtos.product.RemoveProductSizeRequest;
import com.miftah.lamaecommerse.dtos.product.UpdateProductRequest;
import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.BadRequestException;
import com.miftah.lamaecommerse.exceptions.InternalErrorException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.Product;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.services.product.ProductCategoryServiceImpl;
import com.miftah.lamaecommerse.services.product.ProductService;
import com.miftah.lamaecommerse.services.product.ProductSizeServiceImpl;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;

	@Autowired
	ProductCategoryServiceImpl pCategoryService;

	@Autowired
	ProductSizeServiceImpl pSizeService;

	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> createProduct(@RequestBody @Valid CreateProductRequest product)
			throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product pRequest = new Product(product.name(), product.desc(), product.inStock(), product.price());
			Product createdProduct = productService.create(pRequest);
			baseResponse = new BaseResponse<Product>(HttpStatus.CREATED, "201", "Success", createdProduct);
		} catch (AlreadyExistException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.BAD_REQUEST, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@GetMapping("/{category}")
	ResponseEntity<BaseResponse<List<Product>>> getProductByCategory(
			@PathVariable(name = "category") @Valid String category) throws JsonProcessingException {
		BaseResponse<List<Product>> baseResponse;

		try {
			List<Product> products = productService.getProductsByCategory(category);
			baseResponse = new BaseResponse<List<Product>>(HttpStatus.OK, "200", "Success", products);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<List<Product>>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@GetMapping
	ResponseEntity<BaseResponse<Product>> getProductByNameOrId(
			@RequestParam(name = "name", required = false) String pName,
			@RequestParam(name = "id", required = false) Long pId) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = null;
			if (pName != null) {
				product = productService.getOneProductByName(pName);
			} else if (pId != null) {
				product = productService.getById(pId);
			} else {
				throw new BadRequestException(ResponseMessage.BAD_REQUEST, "invalid query", "400");
			}
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", product);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (BadRequestException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.BAD_REQUEST, "400", "invalid query params", null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product controller");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> updateProduct(@PathVariable(name = "id") @Valid Long pId,
			@RequestBody @Valid UpdateProductRequest request) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);

			product.setBaseId(product.getId());
			if (!request.name().isBlank()) {
				product.setName(request.name());
			}

			if (request.desc() != null) {
				product.setDescription(request.desc());
			}

			if (request.inStock() != null) {
				product.setInStock(request.inStock());
			}

			if (request.price() != null) {
				product.setPrice(request.price());
			}

			if (request.imgUrl() != null) {
				product.setImageUrl(request.imgUrl());
			}

			Product updatedProduct = productService.update(product);
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", updatedProduct);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "404", e.getRespMsg().toString(), null);
		} catch (DataIntegrityViolationException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.BAD_REQUEST, "400", "product name already exist", null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> removeProduct(@PathVariable(name = "id") @Valid long pId)
			throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);

			product.setBaseId(product.getId());
			productService.delete(product);

			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", null);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@PostMapping("/{id}/category")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> addProductCategory(@PathVariable(name = "id") @Valid long pId,
			@RequestBody AddProductCategoryRequest request) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);
			ProductCategory pCategory = pCategoryService.getById(request.categoryId());
			productService.addCategory(product, pCategory);
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", null);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@DeleteMapping("/{id}/category")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> removeProductCategory(@PathVariable(name = "id") @Valid long pId,
			@RequestBody RemoveProductCategoryRequest request) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);
			ProductCategory pCategory = pCategoryService.getById(request.categoryId());
			productService.removeCategory(product, pCategory);
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", null);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@PostMapping("/{id}/size")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> addProductSize(@PathVariable(name = "id") @Valid long pId,
			@RequestBody AddProductSizeRequest request) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);
			ProductSize pSize = pSizeService.getById(request.sizeId());
			productService.addSize(product, pSize);
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", null);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}

	@DeleteMapping("/{id}/size")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<BaseResponse<Product>> removeProductSize(@PathVariable(name = "id") @Valid long pId,
			@RequestBody RemoveProductSizeRequest request) throws JsonProcessingException {
		BaseResponse<Product> baseResponse;

		try {
			Product product = productService.getById(pId);
			ProductSize pSize = pSizeService.getById(request.sizeId());
			productService.removeSize(product, pSize);
			baseResponse = new BaseResponse<Product>(HttpStatus.OK, "200", "Success", null);
		} catch (NotFoundException e) {
			baseResponse = new BaseResponse<Product>(HttpStatus.NOT_FOUND, "400", e.getRespMsg().toString(), null);
		} catch (Exception e) {
			InternalErrorException exp = new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR,
					e.getMessage(), "product service");
			baseResponse = new BaseResponse<Product>(HttpStatus.INTERNAL_SERVER_ERROR, "500",
					exp.getRespMsg().toString(), null);
		}

		return baseResponse.buildResponse();
	}
}
