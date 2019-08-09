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
import com.dbs.loyalty.util.TaskUtil;
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

	@Transactional
	public void taskSave(PromoCategory newData) throws JsonProcessingException {
		if(newData.getId() == null) {
			taskService.saveTaskAdd(TYPE, JsonUtil.toString(newData));
		}else {
			PromoCategory oldData = promoCategoryRepository.getOne(newData.getId());
			promoCategoryRepository.save(true, newData.getId());
			taskService.saveTaskModify(TYPE, oldData.getId(), JsonUtil.toString(oldData), JsonUtil.toString(newData));
		}
	}

	@Transactional
	public void taskDelete(PromoCategory oldData) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, oldData.getId(), JsonUtil.toString(oldData));
		promoCategoryRepository.save(true, oldData.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		PromoCategory data = TaskUtil.getObject(task, PromoCategory.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				data.setCreatedBy(task.getMaker());
				data.setCreatedDate(task.getMadeDate());
				promoCategoryRepository.save(data);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				data.setLastModifiedBy(task.getMaker());
				data.setLastModifiedDate(task.getMadeDate());
				data.setPending(false);
				promoCategoryRepository.save(data);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoCategoryRepository.delete(data);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			promoCategoryRepository.save(false, data.getId());
		}

		taskService.confirm(task);
		return data.getName();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Task task, String error) {
		try {
			if(task.getTaskDataId() != null) {
				promoCategoryRepository.save(false, task.getTaskDataId());
			}

			taskService.save(task, error);
			return error;
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}
	
}
