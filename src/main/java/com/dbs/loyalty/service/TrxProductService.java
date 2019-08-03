package com.dbs.loyalty.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.TrxProduct;
import com.dbs.loyalty.repository.TrxProductRepository;
import com.dbs.loyalty.service.specification.TrxProductSpec;

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
	
	public Page<TrxProduct> findAll(Map<String, String> params, Pageable pageable) {
		return trxProductRepository.findAll(new TrxProductSpec(params), pageable);
	}
	
}
