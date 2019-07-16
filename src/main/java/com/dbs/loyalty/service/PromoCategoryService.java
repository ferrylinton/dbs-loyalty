package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.prm.PromoCategory;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoCategoryRepository;
import com.dbs.loyalty.service.specification.PromoCategorySpec;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCategoryService{
	
	private Sort sortByName = Sort.by("name");
	
	private final ObjectMapper objectMapper;
	
	private final PromoCategoryRepository promoCategoryRepository;

	public Optional<PromoCategory> findById(String id){
		return promoCategoryRepository.findById(id);
	}
	
	public List<PromoCategory> findAll(){
		return promoCategoryRepository.findAll(sortByName);
	}

	public Page<PromoCategory> findAll(Map<String, String> params, Pageable pageable) {
		return promoCategoryRepository.findAll(new  PromoCategorySpec(params), pageable);
	}
	
	public Optional<PromoCategory> findByName(String name) {
		return promoCategoryRepository.findByNameIgnoreCase(name);
	}

	public boolean isNameExist(String id, String name) {
		Optional<PromoCategory> promoCategory = promoCategoryRepository.findByNameIgnoreCase(name);

		if (promoCategory.isPresent()) {
			return (id == null) || (!id.equals(promoCategory.get().getId()));
		}else {
			return false;
		}
	}

	public PromoCategory save(PromoCategory promoCategory) {
		return promoCategoryRepository.save(promoCategory);
	}
	
	public void save(boolean pending, String id) {
		promoCategoryRepository.save(pending, id);
	}
	
	public String execute(Task task) throws IOException {
		PromoCategory promoCategory = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), PromoCategory.class);
		
		if(task.getVerified()) {
			
			if(task.getTaskOperation() == TaskOperation.ADD) {
				promoCategory.setCreatedBy(task.getMaker());
				promoCategory.setCreatedDate(task.getMadeDate());
				promoCategoryRepository.save(promoCategory);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				promoCategory.setLastModifiedBy(task.getMaker());
				promoCategory.setLastModifiedDate(task.getMadeDate());
				promoCategory.setPending(false);
				promoCategoryRepository.save(promoCategory);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoCategoryRepository.delete(promoCategory);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			promoCategoryRepository.save(false, promoCategory.getId());
		}

		return promoCategory.getName();
	}
	
}
