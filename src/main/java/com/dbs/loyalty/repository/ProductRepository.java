package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbs.loyalty.domain.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	@Query(value = "select p.termAndCondition from Product p where id= ?1")
	Optional<String> findTermAndConditionById(String id);

	@Query(value = "select p from Product p "
			+ "JOIN FETCH p.productCategory c "
			+ "where c.id=:productCategoryId")
	List<Product> findByProductCategoryId(@Param("productCategoryId") String productCategoryId);
	
}
