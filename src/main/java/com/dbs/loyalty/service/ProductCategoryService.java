package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.ProductCategory;
import com.dbs.loyalty.repository.ProductCategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductCategoryService{
	
	private Sort sortByName = Sort.by("name");

	private final ProductCategoryRepository productCategoryRepository;

	public Optional<ProductCategory> findById(String id){
		return productCategoryRepository.findById(id);
	}
	
	public List<ProductCategory> findAll(){
		return productCategoryRepository.findAll(sortByName);
	}

}
