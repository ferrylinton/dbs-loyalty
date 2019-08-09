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

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoCategoryRepository;
import com.dbs.loyalty.service.specification.PromoCategorySpec;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.SortUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCategoryService{
	
	private static final String TYPE = PromoCategory.class.getSimpleName();

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
		return promoCategoryRepository.findAll(new PromoCategorySpec(params), pageable);
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
	
	@Transactional
	public void taskSave(PromoCategory newPromoCategory) throws JsonProcessingException {
		if(newPromoCategory.getId() == null) {
			taskService.saveTaskAdd(TYPE, JsonUtil.toStr(newPromoCategory));
		}else {
			PromoCategory oldPromoCategory = promoCategoryRepository.getOne(newPromoCategory.getId());
			promoCategoryRepository.save(true, newPromoCategory.getId());
			taskService.saveTaskModify(TYPE, JsonUtil.toStr(oldPromoCategory), JsonUtil.toStr(newPromoCategory));
		}
	}

	@Transactional
	public void taskDelete(PromoCategory promoCategory) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, JsonUtil.toStr(promoCategory));
		promoCategoryRepository.save(true, promoCategory.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		PromoCategory promoCategory = JsonUtil.toObj((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), PromoCategory.class);
		
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
			String obj = (task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew();
			PromoCategory promoCategory = JsonUtil.toObj(obj, PromoCategory.class);
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				promoCategoryRepository.save(false, promoCategory.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
}
