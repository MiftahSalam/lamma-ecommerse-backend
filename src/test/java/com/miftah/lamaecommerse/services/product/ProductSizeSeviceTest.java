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
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.shared.product.ProductSizeMockData;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class ProductSizeSeviceTest {
	@Autowired
	private ProductSizeServiceImpl productSizeServiceImpl;

	private static ProductSizeServiceImpl productSizeServiceImplStatic;

	@PostConstruct
	void init() {
		productSizeServiceImplStatic = productSizeServiceImpl;
	}

	@Test
	@Order(1)
	void testCreateSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			int i = 0;
			for (ProductSize productSize : ProductSizeMockData.productSizes) {
				log.info("Create product size {}", productSize);

				ProductSize createProductSize = productSizeServiceImpl.create(productSize);

				assertThat(createProductSize.getName()).isEqualTo(productSize.getName());
				createProductSize.setBaseId(createProductSize.getId());
				ProductSizeMockData.productSizes.set(i, createProductSize);
				i++;
			}
		});
	}

	@Test
	@Order(2)
	void testCreateProductSizey_FailedAlreadyExist() {
		assertThatExceptionOfType(AlreadyExistException.class).isThrownBy(() -> {
			for (ProductSize productSize : ProductSizeMockData.productSizes) {
				log.info("create already existing product size {}", productSize);

				ProductSize createdProductSize = productSizeServiceImpl.create(productSize);
				assertThat(createdProductSize.getName()).isEqualTo(productSize.getName());
			}
		});
	}

	@Test
	@Order(3)
	void testGetOneProductSizeById_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductSize productSize = productSizeServiceImpl.getById(ProductSizeMockData.productSizes.get(0).getId());
			productSize.setBaseId(productSize.getId());
			assertEquals(productSize, ProductSizeMockData.productSizes.get(0));
		});
	}

	@Test
	@Order(4)
	void testGetOneProductSizeById_NotFound() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			ProductSize productSize = productSizeServiceImpl.getById((long) 2_000_000);
			assertEquals(productSize, ProductSizeMockData.productSizes.get(0));
		});
	}

	@Test
	@Order(5)
	void testGetOneProductSizeById_NullId() {
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() -> {
			ProductSize productSize = productSizeServiceImpl.getById(null);
			assertEquals(productSize, ProductSizeMockData.productSizes.get(0));
		});
	}

	@Test
	@Order(6)
	void testGetOneProductSizeByName_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductSize productSize = productSizeServiceImpl
					.getProductSizeByName(ProductSizeMockData.productSizes.get(0).getName());
			productSize.setBaseId(productSize.getId());
			assertEquals(productSize, ProductSizeMockData.productSizes.get(0));
		});
	}

	@Test
	@Order(7)
	void testGetAllProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			List<ProductSize> productSizes = productSizeServiceImpl.getAll();
			assertTrue(productSizes.size() >= ProductSizeMockData.productSizes.size());
		});
	}

	@Test
	@Order(8)
	void testDeleteOneProductSize_Success() {
		assertThatNoException().isThrownBy(() -> {
			ProductSize productSize = productSizeServiceImpl.delete(ProductSizeMockData.productSizes.get(0));
			productSize.setBaseId(productSize.getId());
			assertEquals(productSize, ProductSizeMockData.productSizes.get(0));
		});
	}

	@AfterAll
	static void cleaningUp() {
		assertThatNoException().isThrownBy(() -> {
			productSizeServiceImplStatic.deleteMany(ProductSizeMockData.productSizes);
		});
	}
}
