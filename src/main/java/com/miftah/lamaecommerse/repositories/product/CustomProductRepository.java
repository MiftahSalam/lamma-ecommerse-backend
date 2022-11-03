package com.miftah.lamaecommerse.repositories.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomProductRepository<Model> extends JpaRepository<Model, Long> {
}
