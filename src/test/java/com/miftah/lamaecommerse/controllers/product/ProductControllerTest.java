package com.miftah.lamaecommerse.controllers.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miftah.lamaecommerse.controllers.AbstractControllerTest;
import com.miftah.lamaecommerse.dtos.BaseResponse;
import com.miftah.lamaecommerse.dtos.product.AddProductCategoryRequest;
import com.miftah.lamaecommerse.dtos.product.AddProductSizeRequest;
import com.miftah.lamaecommerse.dtos.product.CreateProductRequest;
import com.miftah.lamaecommerse.dtos.product.RemoveProductCategoryRequest;
import com.miftah.lamaecommerse.dtos.product.RemoveProductSizeRequest;
import com.miftah.lamaecommerse.dtos.product.UpdateProductRequest;
import com.miftah.lamaecommerse.dtos.user.UserView;
import com.miftah.lamaecommerse.models.product.Product;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.services.auth.JwtService;
import com.miftah.lamaecommerse.services.product.ProductCategoryServiceImpl;
import com.miftah.lamaecommerse.services.product.ProductService;
import com.miftah.lamaecommerse.services.product.ProductSizeServiceImpl;
import com.miftah.lamaecommerse.shared.product.CategoryMockData;
import com.miftah.lamaecommerse.shared.product.ProductMockData;
import com.miftah.lamaecommerse.shared.product.ProductSizeMockData;

import lombok.extern.slf4j.Slf4j;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class ProductControllerTest extends AbstractControllerTest {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryServiceImpl productCategoryService;

	@Autowired
	private ProductSizeServiceImpl productSizeService;

	@Autowired
	JwtService jwtService;

	private String token;
	private String uri = "/product";

	@BeforeAll
	void init() {
		super.setup();

		boolean initOk = true;
		int i = 0;
		for (ProductCategory productCategory : CategoryMockData.categoryList) {
			log.info("create category {}", productCategory);

			try {
				ProductCategory createdCat = productCategoryService.create(productCategory);
				createdCat.setBaseId(createdCat.getId());
				CategoryMockData.categoryList.set(i, createdCat);
				i++;

			} catch (Exception e) {
				initOk = false;
			}
		}

		i = 0;
		for (ProductSize productSize : ProductSizeMockData.productSizes) {
			log.info("create product size {}", productSize);

			try {
				ProductSize createdsize = productSizeService.create(productSize);
				createdsize.setBaseId(createdsize.getId());
				ProductSizeMockData.productSizes.set(i, createdsize);
				i++;

			} catch (Exception e) {
				initOk = false;
			}
		}

		assumeTrue(initOk);
	}

	private void grantedAccess() {
		this.token = "";

		UserView userLogin;
		try {
			userLogin = jwtService.generateToken("user1", "123456");
			this.token = userLogin.token();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void ungrantedAccess() {
		this.token = "";

		UserView userLogin;
		try {
			userLogin = jwtService.generateToken("user2", "123456");
			this.token = userLogin.token();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	@Order(1)
	void testCreateProduct_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();
			int i = 0;
			for (Product product : ProductMockData.products) {
				CreateProductRequest pRequest = new CreateProductRequest(product.getName(), product.getDescription(),
						product.getInStock(), product.getPrice(), product.getImageUrl());

				String productStrJson = super.mapToJson(pRequest);
				MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + this.token)
								.contentType(MediaType.APPLICATION_JSON_VALUE).content(productStrJson))
						.andReturn();

				int status = mvcResult.getResponse().getStatus();
				String content = mvcResult.getResponse().getContentAsString();
				TypeToken<BaseResponse<Product>> typeToken = new TypeToken<BaseResponse<Product>>() {
				};
				Gson gson = new Gson();
				BaseResponse<Product> createdProductResp = gson.fromJson(content, typeToken.getType());
				Product createdProduct = createdProductResp.getData();
				CreateProductRequest productRequestCreated = new CreateProductRequest(createdProduct.getName(),
						createdProduct.getDescription(), createdProduct.getInStock(), createdProduct.getPrice(),
						createdProduct.getImageUrl());

				ProductMockData.products.set(i, createdProduct);

				log.info("content {}", content);
				i++;

				assertEquals(201, status);
				assertThat(productRequestCreated).isEqualTo(pRequest);
			}
		});
	}

	@Test
	@Order(2)
	void testCreateProduct_AlreadyExist() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();
			for (Product product : ProductMockData.products) {
				CreateProductRequest pRequest = new CreateProductRequest(product.getName(), product.getDescription(),
						product.getInStock(), product.getPrice(), product.getImageUrl());

				String productStrJson = super.mapToJson(pRequest);
				MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + this.token)
								.contentType(MediaType.APPLICATION_JSON_VALUE).content(productStrJson))
						.andReturn();

				int status = mvcResult.getResponse().getStatus();
				String content = mvcResult.getResponse().getContentAsString();

				log.info("content {}", content);

				assertEquals(400, status);
			}
		});
	}

	@Test
	@Order(3)
	void testCreateProduct_Forbidden() {
		assertThatNoException().isThrownBy(() -> {
			ungrantedAccess();
			CreateProductRequest pRequest = new CreateProductRequest("product.getDescription()",
					"product.getDescription()", true, 231222.4f, null);

			String productStrJson = super.mapToJson(pRequest);
			MvcResult mvcResult = mvc
					.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + this.token)
							.contentType(MediaType.APPLICATION_JSON_VALUE).content(productStrJson))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(403, status);
		});
	}

	@Test
	@Order(4)
	void testCreateProduct_NotAuthorized() {
		assertThatNoException().isThrownBy(() -> {
			CreateProductRequest pRequest = new CreateProductRequest("product.getDescription()",
					"product.getDescription()", true, 231222.4f, null);

			String productStrJson = super.mapToJson(pRequest);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(401, status);
		});
	}

	@Test
	@Order(5)
	void testAddProductCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(0);
			ProductCategory pCategory = CategoryMockData.categoryList.get(0);
			AddProductCategoryRequest request = new AddProductCategoryRequest(pCategory.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/category", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(200, status);

		});
	}

	@Test
	@Order(6)
	void testAddProductCategory_ProductNotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			ProductCategory pCategory = CategoryMockData.categoryList.get(0);
			AddProductCategoryRequest request = new AddProductCategoryRequest(pCategory.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/category", (long) 2_000_000)
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);

		});
	}

	@Test
	@Order(7)
	void testAddProductCategory_CategoryNotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(0);
			AddProductCategoryRequest request = new AddProductCategoryRequest((long) 2_000_000);

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/category", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);

		});
	}

	@Test
	@Order(8)
	void testAddProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			ProductSize pSize = ProductSizeMockData.productSizes.get(0);
			AddProductSizeRequest request = new AddProductSizeRequest(pSize.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/size", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(200, status);

		});
	}

	@Test
	@Order(9)
	void testAddProductSize_ProductNotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			ProductSize pSize = ProductSizeMockData.productSizes.get(0);
			AddProductSizeRequest request = new AddProductSizeRequest(pSize.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/size", (long) 2_000_000)
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);

		});
	}

	@Test
	@Order(10)
	void testAddProductSize_SizeNotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			AddProductSizeRequest request = new AddProductSizeRequest((long) 2_000_000);

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/{id}/size", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);

		});
	}

	@Test
	@Order(11)
	void testGetProductByCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductCategory pCategory = CategoryMockData.categoryList.get(0);

			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/{category}", pCategory.getName())
					.accept(MediaType.APPLICATION_JSON)).andReturn();
			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();
			TypeToken<BaseResponse<List<Product>>> typeToken = new TypeToken<BaseResponse<List<Product>>>() {
			};
			Gson gson = new Gson();
			BaseResponse<List<Product>> getProductsResp = gson.fromJson(content, typeToken.getType());
			List<Product> getProducts = getProductsResp.getData();

			log.info("content {}", content);

			assertEquals(200, status);

			for (Product product : getProducts) {
				assertTrue(product.getCategories().contains(pCategory));
				assertFalse(product.getCategories().contains(CategoryMockData.categoryList.get(1)));
			}
		});
	}

	@Test
	@Order(12)
	void testGetOneProductByName_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			MvcResult mvcResult = mvc.perform(
					MockMvcRequestBuilders.get(uri).param("name", product.getName()).accept(MediaType.APPLICATION_JSON))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			TypeToken<BaseResponse<Product>> typeToken = new TypeToken<BaseResponse<Product>>() {
			};
			Gson gson = new Gson();
			BaseResponse<Product> getProductsResp = gson.fromJson(content, typeToken.getType());
			Product getProduct = getProductsResp.getData();

			log.info("content {}", content);

			assertEquals(200, status);
			assertEquals(product, getProduct);
		});
	}

	@Test
	@Order(13)
	void testGetOneProductById_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("id", product.getId().toString())
					.accept(MediaType.APPLICATION_JSON)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			TypeToken<BaseResponse<Product>> typeToken = new TypeToken<BaseResponse<Product>>() {
			};
			Gson gson = new Gson();
			BaseResponse<Product> getProductsResp = gson.fromJson(content, typeToken.getType());
			Product getProduct = getProductsResp.getData();

			log.info("content {}", content);

			assertEquals(200, status);
			assertEquals(product, getProduct);
		});
	}

	@Test
	@Order(14)
	void testGetOneProductByNameOrId_NoParameter() {
		assertThatNoException().isThrownBy(() -> {
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(400, status);
		});
	}

	@Test
	@Order(15)
	void testRemoveProductCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(0);
			ProductCategory pCategory = CategoryMockData.categoryList.get(0);
			RemoveProductCategoryRequest request = new RemoveProductCategoryRequest(pCategory.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri + "/{id}/category", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(200, status);

		});
	}

	@Test
	@Order(16)
	void testRemoveProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			ProductSize pSize = ProductSizeMockData.productSizes.get(0);
			RemoveProductSizeRequest request = new RemoveProductSizeRequest(pSize.getId());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri + "/{id}/size", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(200, status);

		});
	}

	@Test
	@Order(17)
	void testUpdateProduct_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			Product updateProduct = new Product(product.getName() + " Update", "update description", true, 34222.4f);

			updateProduct.setImageUrl("http://dsfsdfs.5434");
			updateProduct.setId(product.getId());

			UpdateProductRequest request = new UpdateProductRequest(updateProduct.getName(),
					updateProduct.getDescription(), updateProduct.getInStock(), updateProduct.getPrice(),
					updateProduct.getImageUrl());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/{id}", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();
			TypeToken<BaseResponse<Product>> typeToken = new TypeToken<BaseResponse<Product>>() {
			};
			Gson gson = new Gson();
			BaseResponse<Product> getProductsResp = gson.fromJson(content, typeToken.getType());
			Product updatedProduct = getProductsResp.getData();

			log.info("content {}", content);

			assertEquals(200, status);
			assertEquals(updateProduct, updatedProduct);

		});
	}

	@Test
	@Order(18)
	void testUpdateProduct_NotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			Product updateProduct = new Product(product.getName(), "update description", true, 34222.4f);

			updateProduct.setImageUrl("http://dsfsdfs.5434");
			updateProduct.setId(product.getId());

			UpdateProductRequest request = new UpdateProductRequest(product.getName(), updateProduct.getDescription(),
					updateProduct.getInStock(), updateProduct.getPrice(), updateProduct.getImageUrl());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/{id}", (long) 2_000_000)
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);
		});
	}

	@Test
	@Order(19)
	void testUpdateProduct_DuplicateProductName() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			Product updateProduct = new Product(product.getName(), "update description", true, 34222.4f);

			updateProduct.setImageUrl("http://dsfsdfs.5434");
			updateProduct.setId(product.getId());

			UpdateProductRequest request = new UpdateProductRequest(ProductMockData.products.get(1).getName(),
					updateProduct.getDescription(), updateProduct.getInStock(), updateProduct.getPrice(),
					updateProduct.getImageUrl());

			String productStrJson = super.mapToJson(request);
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/{id}", product.getId())
					.header("Authorization", "Bearer " + this.token).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(productStrJson)).andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(400, status);
		});
	}

	@Test
	@Order(20)
	void testDeleteOneProduct_Success() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			MvcResult mvcResult = mvc
					.perform(MockMvcRequestBuilders.delete(uri + "/{id}", product.getId())
							.header("Authorization", "Bearer " + this.token).accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(200, status);
		});
	}

	@Test
	@Order(21)
	void testDeleteOneProduct_NotFound() {
		assertThatNoException().isThrownBy(() -> {
			grantedAccess();

			Product product = ProductMockData.products.get(2);
			MvcResult mvcResult = mvc
					.perform(MockMvcRequestBuilders.delete(uri + "/{id}", product.getId())
							.header("Authorization", "Bearer " + this.token).accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(404, status);
		});
	}

	@Test
	@Order(22)
	void testDeleteOneProduct_NotAuthorized() {
		assertThatNoException().isThrownBy(() -> {
			ungrantedAccess();

			Product product = ProductMockData.products.get(0);
			MvcResult mvcResult = mvc
					.perform(MockMvcRequestBuilders.delete(uri + "/{id}", product.getId())
							.header("Authorization", "Bearer " + this.token).accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();

			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString();

			log.info("content {}", content);

			assertEquals(403, status);
		});
	}

	@AfterAll
	void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
			productService.deleteMany(ProductMockData.products);
		});
		assertThatNoException().isThrownBy(() -> {
			productSizeService.deleteMany(ProductSizeMockData.productSizes);
		});
		assertThatNoException().isThrownBy(() -> {
			productCategoryService.deleteMany(CategoryMockData.categoryList);
		});
	}

}
