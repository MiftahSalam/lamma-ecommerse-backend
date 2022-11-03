package com.miftah.lamaecommerse.services.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import org.springframework.boot.test.context.SpringBootTest;

import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.Product;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.shared.product.CategoryMockData;
import com.miftah.lamaecommerse.shared.product.ProductMockData;
import com.miftah.lamaecommerse.shared.product.ProductSizeMockData;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductServiceTest {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryServiceImpl productCategoryService;

	@Autowired
	private ProductSizeServiceImpl productSizeService;

	@BeforeAll
	void init() {
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

	@Test
	@Order(1)
	void testCreateProduct_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (Product product : ProductMockData.products) {
				log.info("Create product {}", product);

				Product createProduct = productService.create(product);

				assertThat(createProduct.getName()).isEqualTo(product.getName());
				createProduct.setBaseId(createProduct.getId());
				ProductMockData.products.set(i, createProduct);
				i++;
			}
		});
	}

	@Test
	@Order(2)
	void testCreateProduct_FailedAlreadyExist() {
		for (Product product : ProductMockData.products) {
			log.info("create already existing product {}", product);

			assertThatExceptionOfType(AlreadyExistException.class).isThrownBy(() -> {
				Product createdProduct = productService.create(product);
				assertThat(createdProduct.getName()).isEqualTo(product.getName());
			});
		}
	}

	@Test
//	@Disabled
	@Order(3)
	void testAddProductCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			productService.addCategory(product, CategoryMockData.categoryList.get(0));
			Product getProduct = productService.getById(product.getId());
			product.setCategories(getProduct.getCategories());
			ProductMockData.products.set(0, product);

			assertTrue(getProduct.getCategories().size() > 0);
			assertTrue(product.getCategories().contains(CategoryMockData.categoryList.get(0)));
		});

		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(1);
			productService.addCategory(product, CategoryMockData.categoryList.get(1));
			Product getProduct = productService.getById(product.getId());
			product.setCategories(getProduct.getCategories());
			ProductMockData.products.set(1, product);

			productService.addCategory(product, CategoryMockData.categoryList.get(3));
			getProduct = productService.getById(product.getId());
			product.setCategories(getProduct.getCategories());
			ProductMockData.products.set(1, product);

			assertTrue(getProduct.getCategories().size() > 0);
			assertTrue(product.getCategories().contains(CategoryMockData.categoryList.get(1)));
			assertTrue(product.getCategories().contains(CategoryMockData.categoryList.get(3)));
		});
	}

	@Test
//	@Disabled
	@Order(4)
	void testAddProductCategory_CategoryNotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			ProductCategory productCategory = new ProductCategory("not found");

			productCategory.setBaseId((long) 2_000_000);
			productCategory.setId((long) 2_000_000);
			productService.addCategory(product, productCategory);
		});
	}

	@Test
	@Order(5)
//	@Transactional
	void testAddProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			productService.addSize(product, ProductSizeMockData.productSizes.get(0));
			Product getProduct = productService.getById(product.getId());
			product.setSizes(getProduct.getSizes());
			ProductMockData.products.set(0, product);

			assertTrue(getProduct.getSizes().size() > 0);
			assertTrue(product.getSizes().contains(ProductSizeMockData.productSizes.get(0)));
		});

		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			productService.addSize(product, ProductSizeMockData.productSizes.get(0));
			Product getProduct = productService.getById(product.getId());
			product.setSizes(getProduct.getSizes());
			ProductMockData.products.set(2, product);

			productService.addSize(product, ProductSizeMockData.productSizes.get(2));
			getProduct = productService.getById(product.getId());
			product.setSizes(getProduct.getSizes());
			ProductMockData.products.set(2, product);

			assertTrue(getProduct.getSizes().size() > 1);
			assertTrue(product.getSizes().contains(ProductSizeMockData.productSizes.get(0)));
			assertTrue(product.getSizes().contains(ProductSizeMockData.productSizes.get(2)));
		});
	}

	@Test
	@Order(6)
	void testAddProductSize_SizeNotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			ProductSize productSize = new ProductSize("xxxx");

			productSize.setBaseId((long) 2_000_000);
			productSize.setId((long) 2_000_000);
			productService.addSize(product, productSize);
		});
	}

	@Test
	@Order(7)
	void testRemoveProductCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			Product getProduct = productService.getById(product.getId());

			assertTrue(getProduct.getCategories().contains(CategoryMockData.categoryList.get(0)));

			productService.removeCategory(product, CategoryMockData.categoryList.get(0));
			getProduct = productService.getById(product.getId());

			assertFalse(getProduct.getCategories().contains(CategoryMockData.categoryList.get(0)));
		});
	}

	@Test
	@Order(8)
	void testRemoveProductCategory_CategoryNotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			Product product = ProductMockData.products.get(0);
			ProductCategory productCategory = new ProductCategory("xxxx");

			productCategory.setBaseId((long) 2_000_000);
			productCategory.setId((long) 2_000_000);
			productService.removeCategory(product, productCategory);
		});
	}

	@Test
	@Order(9)
	void testRemoveProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			Product getProduct = productService.getById(product.getId());

			assertTrue(getProduct.getSizes().contains(ProductSizeMockData.productSizes.get(2)));

			productService.removeSize(product, ProductSizeMockData.productSizes.get(2));
			getProduct = productService.getById(product.getId());

			assertTrue(getProduct.getSizes().contains(ProductSizeMockData.productSizes.get(0)));
			assertFalse(getProduct.getSizes().contains(ProductSizeMockData.productSizes.get(2)));
		});
	}

	@Test
	@Order(10)
	void testGetOneProductByName_Success() {
		assertThatNoException().isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			Product getProduct = productService.getOneProductByName(product.getName());

			assertEquals(getProduct.getName(), product.getName());
			assertEquals(getProduct.getCategories(), product.getCategories());
		});
	}

	@Test
	@Order(11)
	void testGetOneProductByName_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			Product product = ProductMockData.products.get(2);
			Product getProduct = productService.getOneProductByName("product.getName()");

			assertEquals(getProduct.getName(), product.getName());
			assertEquals(getProduct.getCategories(), product.getCategories());
		});
	}

	@Test
	@Order(12)
	void testGetProductsByCat_Success() {
		assertThatNoException().isThrownBy(() -> {
			List<Product> getProducts = productService
					.getProductsByCategory(CategoryMockData.categoryList.get(1).getName());

			assertTrue(getProducts.size() > 0);
		});

		assertThatNoException().isThrownBy(() -> {
			List<Product> getProducts = productService
					.getProductsByCategory(CategoryMockData.categoryList.get(0).getName());

			assertTrue(getProducts.size() == 0);
		});
	}

	@Test
	@Order(13)
	void testGetProductsByCat_NotFound() {
		assertThatNoException().isThrownBy(() -> {
			List<Product> getProducts = productService
					.getProductsByCategory("CategoryMockData.categoryList.get(1).getName()");

			assertTrue(getProducts.size() == 0);
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
