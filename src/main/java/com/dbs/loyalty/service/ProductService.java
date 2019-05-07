package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Product;
import com.dbs.loyalty.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService{

	private final ProductRepository productRepository;

	public Optional<Product> findById(String id){
		return productRepository.findById(id);
	}
	
	public Optional<String> findTermAndConditionById(String id){
		return productRepository.findTermAndConditionById(id);
	}
	
	public List<Product> findByProductCategoryId(String productCategoryId){
		return productRepository.findByProductCategoryId(productCategoryId);
	}
	
}
