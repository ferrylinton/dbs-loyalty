package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.specification.PromoSpecification;
import com.dbs.loyalty.util.Base64Util;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PromoService{
	
	private final ObjectMapper objectMapper;
	
	private final PromoRepository promoRepository;

	private PromoService(ObjectMapper objectMapper, PromoRepository promoRepository) {
		this.objectMapper = objectMapper;
		this.promoRepository = promoRepository;
	}
	
	public Optional<Promo> findById(String id){
		Optional<Promo> promo = promoRepository.findById(id);
		
		if(promo.isPresent()) {
			promo.get().setImageString(Base64Util.getString(promo.get().getImageBytes()));
		}
		
		return promo;
	}
	
	public List<Promo> findAll(Sort sort){
		return promoRepository.findAll(sort);
	}
	
	public Page<Promo> findAll(Pageable pageable, HttpServletRequest request) {
		Specification<Promo> spec = PromoSpecification.getSpec(request);
		return promoRepository.findAll(spec, pageable);
	}

	public List<Promo> findByPromoCategoryId(String promoCategoryId){
		return promoRepository.findByPromoCategoryId(promoCategoryId);
	}
	
	public boolean isCodeExist(Promo promo) {
		Optional<Promo> obj = promoRepository.findByCodeIgnoreCase(promo.getCode());

		if (obj.isPresent()) {
			if (promo.getId() == null) {
				return true;
			} else if (!promo.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}
	
	public boolean isTitleExist(Promo promo) {
		Optional<Promo> obj = promoRepository.findByTitleIgnoreCase(promo.getTitle());

		if (obj.isPresent()) {
			if (promo.getId() == null) {
				return true;
			} else if (!promo.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}

	public String execute(Task task) throws JsonParseException, JsonMappingException, IOException {
		Promo promo = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Promo.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				promo.setImageBytes(Base64Util.getBytes(promo.getImageString()));
				promo.setCreatedBy(task.getMaker());
				promo.setCreatedDate(task.getMadeDate());
				promoRepository.save(promo);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				promo.setImageBytes(Base64Util.getBytes(promo.getImageString()));
				promo.setLastModifiedBy(task.getMaker());
				promo.setLastModifiedDate(task.getMadeDate());
				promoRepository.save(promo);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoRepository.delete(promo);
			}
		}

		return promo.getTitle();
	}
	
}
