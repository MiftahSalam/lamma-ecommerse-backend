package com.miftah.lamaecommerse.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.product.ProductSize;
import com.miftah.lamaecommerse.repositories.product.ProductSizeRepository;
import com.miftah.lamaecommerse.services.BaseServiceImpl;

@Service
public class ProductSizeServiceImpl extends BaseServiceImpl<ProductSize, ProductSizeRepository> {
//	public class ProductSizeServiceImpl implements ProductSizeService {
	@Autowired
	private ProductSizeRepository productSizeRepository;
//
//	@Autowired
//	private EntityManager entityManager;
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Throwable.class })
//	public ProductSize createProductSize(ProductSize pSize) throws BaseException {
//		try {
//			entityManager.persist(pSize);
//
//			return pSize;
//		} catch (PersistenceException e) {
//			throw new AlreadyExistException(ResponseMessage.ALREADYEXIST, e.getMessage(), "user service");
//		} catch (Exception e) {
//			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
//		}
//	}
//
//	@Override
//	public List<ProductSize> getAllSizes() throws BaseException {
//		List<ProductSize> productSizes = productSizeRepository.findAll();
//		if (productSizes.isEmpty()) {
//			throw new NotFoundException(ResponseMessage.NOTFOUND, "Product categories is empty", "product service");
//		} else {
//			return productSizes;
//		}
//	}
//
//	@Override
//	public ProductSize getProductSizeById(Long id) throws BaseException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public ProductSize getProductSizeByName(String name) throws BaseException {
		try {
			ProductSize categorySize = productSizeRepository.findOneByName(name);
			if (categorySize != null) {
				return categorySize;
			} else {
				throw new NotFoundException(ResponseMessage.NOTFOUND, "Product size not found", "product service");
			}
		} catch (Exception e) {
			throw e;
		}
	}
//
//	@Override
//	public ProductSize deleteSize(ProductSize pSize) throws BaseException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void deleteAllSize(List<ProductSize> pSizes) throws BaseException {
//		// TODO Auto-generated method stub
//
//	}
}
