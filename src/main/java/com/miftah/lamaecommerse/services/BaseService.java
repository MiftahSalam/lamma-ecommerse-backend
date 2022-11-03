package com.miftah.lamaecommerse.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miftah.lamaecommerse.exceptions.BaseException;

public interface BaseService<Model, Repo extends JpaRepository<Model, Long>> {
	public Model create(Model entity) throws BaseException;

	public List<Model> getAll() throws BaseException;

	public Model getById(Long id) throws BaseException;

	public Model update(Model entity) throws BaseException;

	public Model delete(Model entity) throws BaseException;

	public void deleteMany(List<Model> entities) throws BaseException;

}
