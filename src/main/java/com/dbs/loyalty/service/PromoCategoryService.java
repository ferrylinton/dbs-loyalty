package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoCategoryRepository;
import com.dbs.loyalty.service.specification.PromoCategorySpecification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PromoCategoryService{
	
	private final ObjectMapper objectMapper;
	
	private final PromoCategoryRepository promoCategoryRepository;

	private PromoCategoryService(ObjectMapper objectMapper, PromoCategoryRepository promoCategoryRepository) {
		this.objectMapper = objectMapper;
		this.promoCategoryRepository = promoCategoryRepository;
	}
	
	public Optional<PromoCategory> findById(String id){
		return promoCategoryRepository.findById(id);
	}
	
	public List<PromoCategory> findAll(Sort sort){
		return promoCategoryRepository.findAll(sort);
	}

	public Page<PromoCategory> findAll(Pageable pageable, HttpServletRequest request) {
		return promoCategoryRepository.findAll(PromoCategorySpecification.getSpec(request), pageable);
	}
	
	public Optional<PromoCategory> findByName(String name) {
		return promoCategoryRepository.findByNameIgnoreCase(name);
	}

	public boolean isNameExist(PromoCategory promoCategory) {
		Optional<PromoCategory> obj = promoCategoryRepository.findByNameIgnoreCase(promoCategory.getName());

		if (obj.isPresent()) {
			if (promoCategory.getId() == null) {
				return true;
			} else if (!promoCategory.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}

	public String execute(Task task) throws JsonParseException, JsonMappingException, IOException {
		PromoCategory promoCategory = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), PromoCategory.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				promoCategory.setCreatedBy(task.getMaker());
				promoCategory.setCreatedDate(task.getMadeDate());
				promoCategoryRepository.save(promoCategory);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				promoCategory.setLastModifiedBy(task.getMaker());
				promoCategory.setLastModifiedDate(task.getMadeDate());
				promoCategoryRepository.save(promoCategory);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoCategoryRepository.delete(promoCategory);
			}
		}

		return promoCategory.getName();
	}
	
}
