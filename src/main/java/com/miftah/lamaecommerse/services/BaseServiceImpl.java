package com.miftah.lamaecommerse.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.miftah.lamaecommerse.dtos.ResponseMessage;
import com.miftah.lamaecommerse.exceptions.AlreadyExistException;
import com.miftah.lamaecommerse.exceptions.BaseException;
import com.miftah.lamaecommerse.exceptions.InternalErrorException;
import com.miftah.lamaecommerse.exceptions.NotFoundException;
import com.miftah.lamaecommerse.models.BaseModel;

@Service
public abstract class BaseServiceImpl<Model, Repo extends JpaRepository<Model, Long>>
		implements BaseService<Model, JpaRepository<Model, Long>> {
	@Autowired
	protected Repo repository;

	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Throwable.class })
	public Model create(Model entity) throws BaseException {
		try {
			entityManager.persist(entity);
			return entity;
		} catch (PersistenceException e) {
			throw new AlreadyExistException(ResponseMessage.ALREADYEXIST, e.getMessage(), "base service");
		} catch (Exception e) {
			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "base service");
		}
	}

	@Override
	public List<Model> getAll() throws BaseException {
		List<Model> entities = repository.findAll();
		if (entities.isEmpty()) {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "data empty", "user service");
		} else {
			return entities;
		}
	}

	@Override
	public Model getById(Long id) throws BaseException {
		Optional<Model> entity = repository.findById(id);
		if (entity.isEmpty()) {
			throw new NotFoundException(ResponseMessage.NOTFOUND, "data not found", "user service");
		} else {
			return entity.get();
		}
	}

	@Override
	public Model update(Model entity) throws BaseException {
		try {
			BaseModel model = (BaseModel) entity;
			getById(model.getBaseId());
			Model updatedModel = repository.save(entity);
			BaseModel updatedBaseModel = (BaseModel) updatedModel;
			updatedBaseModel.setBaseId(model.getBaseId());
			return updatedModel;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Model delete(Model entity) throws BaseException {
		try {
			BaseModel model = (BaseModel) entity;
			Model modelToDelete = getById(model.getBaseId());
			repository.delete(modelToDelete);
			return modelToDelete;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void deleteMany(List<Model> entities) throws BaseException {
		try {
			repository.deleteAllInBatch(entities);
		} catch (Exception e) {
			throw new InternalErrorException(ResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage(), "user service");
		}
	}
}
