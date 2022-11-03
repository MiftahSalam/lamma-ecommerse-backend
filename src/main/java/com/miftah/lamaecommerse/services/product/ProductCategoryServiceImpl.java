package com.miftah.lamaecommerse.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.ProductCategory;
import com.miftah.lamaecommerse.repositories.product.ProductCategoryRepository;
import com.miftah.lamaecommerse.services.BaseServiceImpl;

@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory, ProductCategoryRepository> {
//	public class ProductCategoryServiceImpl implements ProductCategoryService {
//	private static final String TABLE_NAME = ProductCategory.class.getAnnotation(Table.class).name();
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

//	@Autowired
//	private EntityManager entityManager;
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Throwable.class })
//	public ProductCategory createProductCategory(ProductCategory category) throws BaseException {
//		try {
//			entityManager.persist(category);
//
//			return category;
//		} catch (PersistenceException e) {
//			throw new AlreadyExistException(ResponseMessage.ALREADYEXIST, e.getMessage(), "user service");
//		} catch (Exception e) {
//			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
//		}
//	}
//
//	@Override
//	public List<ProductCategory> getAllCategories() throws BaseException {
//		List<ProductCategory> productCategories = productCategoryRepository.findAll();
//		if (productCategories.isEmpty()) {
//			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product categories is empty", "product service");
//		} else {
//			return productCategories;
//		}
//	}
//
//	@Override
//	public ProductCategory deleteCategory(ProductCategory category) throws BaseException {
//		try {
//			Optional<ProductCategory> categoryToDelete = productCategoryRepository.findById(category.getId());
//			categoryToDelete.ifPresentOrElse(cat -> productCategoryRepository.delete(cat),
//					() -> new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found",
//							"product service"));
//			;
//			return categoryToDelete.get();
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Override
//	public void deleteAllCategories(List<ProductCategory> categories) throws BaseException {
//		try {
//			productCategoryRepository.deleteAll(categories);
//		} catch (Exception e) {
//			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
//		}
//	}
//
//	@Override
//	public ProductCategory getProductCategoryById(Long id) throws BaseException {
//		try {
//			Optional<ProductCategory> category = productCategoryRepository.findById(id);
//			if (category.isPresent()) {
//				return category.get();
//			} else {
//				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//	}

//	@Override
	public ProductCategory getProductCategoryByName(String name) throws BaseException {
		try {
			ProductCategory category = productCategoryRepository.findOneByName(name);
			if (category != null) {
				return category;
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product category not found", "product service");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
