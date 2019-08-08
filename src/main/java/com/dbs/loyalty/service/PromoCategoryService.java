package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoCategoryRepository;
import com.dbs.loyalty.service.specification.PromoCategorySpec;
import com.dbs.loyalty.util.SortUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCategoryService{

	private final ObjectMapper objectMapper;
	
	private final PromoCategoryRepository promoCategoryRepository;
	
	private final TaskService taskService;

	public Optional<PromoCategory> findById(String id){
		return promoCategoryRepository.findById(id);
	}
	
	public PromoCategory getOne(String id){
		return promoCategoryRepository.getOne(id);
	}
	
	public List<PromoCategory> findAll(){
		return promoCategoryRepository.findAll(SortUtil.SORT_BY_NAME);
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
	
	@Transactional
	public void taskSave(PromoCategory newPromoCategory) throws JsonProcessingException {
		if(newPromoCategory.getId() == null) {
			taskService.saveTaskAdd(DomainConstant.ROLE, toString(newPromoCategory));
		}else {
			PromoCategory oldPromoCategory = promoCategoryRepository.getOne(newPromoCategory.getId());
			promoCategoryRepository.save(true, newPromoCategory.getId());
			taskService.saveTaskModify(DomainConstant.ROLE, toString(oldPromoCategory), toString(newPromoCategory));
		}
	}

	@Transactional
	public void taskDelete(PromoCategory promoCategory) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.ROLE, toString(promoCategory));
		promoCategoryRepository.save(true, promoCategory.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
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
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Exception ex, Task task) {
		try {
			PromoCategory promoCategory = toPromoCategory((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				promoCategoryRepository.save(false, promoCategory.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	private String toString(PromoCategory promoCategory) throws JsonProcessingException {
		return objectMapper.writeValueAsString(promoCategory);
	}
	
	private PromoCategory toPromoCategory(String content) throws IOException {
		return objectMapper.readValue(content, PromoCategory.class);
	}
	
}
