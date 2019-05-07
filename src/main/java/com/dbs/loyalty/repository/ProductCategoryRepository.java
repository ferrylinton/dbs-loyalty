package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String>{

}
