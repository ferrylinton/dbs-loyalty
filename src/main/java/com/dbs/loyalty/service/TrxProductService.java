package com.dbs.loyalty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.TrxProduct;
import com.dbs.loyalty.repository.TrxProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TrxProductService{

	private final TrxProductRepository trxProductRepository;

	public Optional<TrxProduct> findById(String id){
		return trxProductRepository.findById(id);
	}

	public List<TrxProduct> findAll(){
		return trxProductRepository.findAll();
	}
	
}
