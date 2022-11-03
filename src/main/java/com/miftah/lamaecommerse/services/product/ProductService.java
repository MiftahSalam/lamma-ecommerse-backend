package com.miftah.lamaecommerse.services.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.Product;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.repositories.product.ProductCategoryRepository;
import com.miftah.lamaecommerse.repositories.product.ProductRepository;
import com.miftah.lamaecommerse.repositories.product.ProductSizeRepository;
import com.miftah.lamaecommerse.services.BaseServiceImpl;

@Service
public class ProductService extends BaseServiceImpl<Product, ProductRepository> {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductSizeRepository productSizeRepository;

	public void addCategory(Product product, ProductCategory category) throws Exception {
		Optional<Product> getProduct = productRepository.findById(product.getId());
		if (getProduct.isPresent()) {
			Optional<ProductCategory> getProductCat = productCategoryRepository.findById(category.getId());
			if (getProductCat.isPresent()) {
				try {
					getProduct.get().addCategory(category);
					productRepository.saveAndFlush(getProduct.get());
				} catch (Exception e) {
					throw e;
				}
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} else {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product not found", "product service");
		}
	}

	public void addSize(Product product, ProductSize productSize) throws Exception {
		Optional<Product> getProduct = productRepository.findById(product.getId());
		if (getProduct.isPresent()) {
			Optional<ProductSize> getProductSize = productSizeRepository.findById(productSize.getId());
			if (getProductSize.isPresent()) {
				try {
					getProduct.get().addSize(productSize);
					productRepository.saveAndFlush(getProduct.get());
				} catch (Exception e) {
					throw e;
				}
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} else {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product not found", "product service");
		}
	}

	public void removeCategory(Product product, ProductCategory category) throws Exception {
		Optional<Product> getProduct = productRepository.findById(product.getId());
		if (getProduct.isPresent()) {
			Optional<ProductCategory> getProductCat = productCategoryRepository.findById(category.getId());
			if (getProductCat.isPresent()) {
				try {
					getProduct.get().removeCategory(category);
					productRepository.save(getProduct.get());
				} catch (Exception e) {
					throw e;
				}
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} else {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product not found", "product service");
		}
	}

	public void removeSize(Product product, ProductSize productSize) throws Exception {
		Optional<Product> getProduct = productRepository.findById(product.getId());
		if (getProduct.isPresent()) {
			Optional<ProductSize> getProductCat = productSizeRepository.findById(productSize.getId());
			if (getProductCat.isPresent()) {
				try {
					getProduct.get().removeSize(productSize);
					productRepository.save(getProduct.get());
				} catch (Exception e) {
					throw e;
				}
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} else {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product not found", "product service");
		}
	}

	public Product getOneProductByName(String name) throws BaseException {
		try {
			Product product = productRepository.findOneByName(name);
			if (product != null) {
				return product;
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Product> getProductsByCategory(String categoryName) throws BaseException {
		try {
			List<Product> products = productRepository.findByCategoryName(categoryName);
			if (products != null) {
				return products;
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// public void tes() throws Exception {
//		addOrRemove(null, null, null);
//	}
//	private <T extends BaseModel ,R extends JpaRepository<T, Long>> void addOrRemove(Product product, T item, R repo) throws Exception {
//		Optional<Product> getProduct = productRepository.findById(product.getId());
//		if (getProduct.isPresent()) {
//			Optional<T> getProductCat = repo.findById(item.getBaseId());
//			if (getProductCat.isPresent()) {
//				try {
//					getProduct.get().removeCategory(category);
//				} catch (Exception e) {
//					throw e;
//				}
//			} else {
//				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
//			}
//		} else {
//			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product not found", "product service");
//		}
//		
//	}	
}