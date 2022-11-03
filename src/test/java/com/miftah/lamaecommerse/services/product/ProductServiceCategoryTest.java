package com.miftah.lamaecommerse.services.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.shared.product.CategoryMockData;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class ProductServiceCategoryTest {
	@Autowired
	private ProductCategoryServiceImpl productService;
//	private ProductCategoryService productService;

	private static ProductCategoryServiceImpl productServiceStatic;
//	private static ProductCategoryService productServiceStatic;

	@PostConstruct
	public void init() {
		productServiceStatic = productService;
	}

	@Test
	@Order(1)
	void testCreateCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (ProductCategory productCategory : CategoryMockData.categoryList) {
				log.info("create category {}", productCategory);

				ProductCategory createdCat = productService.create(productCategory);
//				ProductCategory createdCat = productService.createProductCategory(productCategory);
				assertThat(createdCat.getName()).isEqualTo(productCategory.getName());
				createdCat.setBaseId(createdCat.getId());
				CategoryMockData.categoryList.set(i, createdCat);
				i++;
			}
		});
	}

	@Test
	@Order(2)
	void testCreateCategory_FaileAlreadyExist() {
		assertThatExceptionOfType(AlreadyExistException.class).isThrownBy(() -> {
			for (ProductCategory productCategory : CategoryMockData.categoryList) {
				log.info("create already existing category {}", productCategory);

				ProductCategory createdCat = productService.create(productCategory);
//				ProductCategory createdCat = productService.createProductCategory(productCategory);
				assertThat(createdCat.getName()).isEqualTo(productCategory.getName());
			}
		});
	}

	@Test
//	@Disabled // wait for product feature implementation
	@Order(3)
	void testGetOneCategoryById_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductCategory category = productService.getById(CategoryMockData.categoryList.get(0).getId());
//			.getProductCategoryById(CategoryMockData.categoryList.get(0).getId());
			assertEquals(category, CategoryMockData.categoryList.get(0));
		});
	}

	@Test
	@Order(4)
	void testGetOneCategoryById_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			ProductCategory category = productService.getById((long) 2_000_000);
//			ProductCategory category = productService.getProductCategoryById((long) 2_000_000);
			assertEquals(category, CategoryMockData.categoryList.get(0));
		});
	}

	@Test
	@Order(5)
	void testGetOneCategoryById_NullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> {
			ProductCategory category = productService.getById(null);
//			ProductCategory category = productService.getProductCategoryById(null);
			assertEquals(category, CategoryMockData.categoryList.get(0));
		});
	}

	@Test
//	@Disabled // wait for product feature implementation
	@Order(6)
	void testGetOneCategoryByName_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductCategory category = productService
					.getProductCategoryByName(CategoryMockData.categoryList.get(0).getName());
//			.getProductCategoryByName(CategoryMockData.categoryList.get(0).getName());
			assertEquals(category, CategoryMockData.categoryList.get(0));
		});
	}

	@Test
	@Order(7)
	void testGetAllCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			List<ProductCategory> categories = productService.getAll();
//			List<ProductCategory> categories = productService.getAllCategories();
			assertTrue(categories.size() >= CategoryMockData.categoryList.size());
		});
	}

	@Test
//	@Disabled // wait for product feature implementation
	@Order(8)
	void testDeleteOneCategory_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductCategory category = productService.delete(CategoryMockData.categoryList.get(0));
//			ProductCategory category = productService.deleteCategory(CategoryMockData.categoryList.get(0));
			assertEquals(category, CategoryMockData.categoryList.get(0));
		});
	}

	@AfterAll
	static void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
			productServiceStatic.deleteMany(CategoryMockData.categoryList);
//			productServiceStatic.deleteAllCategories(CategoryMockData.categoryList);
		});
	}

}
