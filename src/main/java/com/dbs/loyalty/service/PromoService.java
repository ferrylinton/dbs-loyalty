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

import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.specification.PromoSpec;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.TaskUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoService{
	
	private static final String TYPE = Promo.class.getSimpleName();

	private final PromoRepository promoRepository;
	
	private final ImageService imageService;

	private final TaskService taskService;

	public Optional<Promo> findById(String id){
		return promoRepository.findById(id);
	}
	
	public Promo getOne(String id){
		return promoRepository.getOne(id);
	}
	
	public Optional<String> findTermAndConditionById(String id){
		return promoRepository.findTermAndConditionById(id);
	}


	public Page<Promo> findAll(Map<String, String> params, Pageable pageable) {
		return promoRepository.findAll(new PromoSpec(params), pageable);
	}

	public List<Promo> findPromoInCarousel(){
		List<Promo> promos = promoRepository.findPromoInCarousel();
		
		if(promos.size() > 3) {
			return promos.subList(0, 3);
		}else {
			return promos;
		}
	}
	
	public List<Promo> findByPromoCategoryId(String promoCategoryId){
		return promoRepository.findByPromoCategoryId(promoCategoryId);
	}
	
	public boolean isCodeExist(String id, String code) {
		Optional<Promo> promo = promoRepository.findByCodeIgnoreCase(code);

		if (promo.isPresent()) {
			return (id == null) || (!id.equals(promo.get().getId()));
		}else {
			return false;
		}
	}
	
	public boolean isTitleExist(String id, String title) {
		Optional<Promo> promo = promoRepository.findByTitleIgnoreCase(title);

		if (promo.isPresent()) {
			return (id == null) || (!id.equals(promo.get().getId()));
		}else {
			return false;
		}
	}
	
	public Promo save(Promo promo) {
		return promoRepository.save(promo);
	}
	
	public void save(boolean pending, String id) {
		promoRepository.save(pending, id);
	}
	
	@Transactional
	public void taskSave(Promo newData) throws IOException {
		if(newData.getId() == null) {
			FileImageTask fileImageTask = imageService.add(newData.getMultipartFileImage());
			newData.setImage(fileImageTask.getId());
			
			taskService.saveTaskAdd(TYPE, JsonUtil.toString(newData));
		}else {
			Promo oldData = promoRepository.getOne(newData.getId());
			
			if(newData.getMultipartFileImage().isEmpty()) {
				newData.setImage(newData.getId());
			}else {
				FileImageTask fileImageTask = imageService.add(newData.getMultipartFileImage());
				newData.setImage(fileImageTask.getId());
			}
			
			oldData.setImage(newData.getId());
			
			promoRepository.save(true, newData.getId());
			taskService.saveTaskModify(TYPE, oldData.getId(), JsonUtil.toString(oldData), JsonUtil.toString(newData));
		}
	}

	@Transactional
	public void taskDelete(Promo data) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, data.getId(), JsonUtil.toString(data));
		promoRepository.save(true, data.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		Promo data = TaskUtil.getObject(task, Promo.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				data.setCreatedBy(task.getMaker());
				data.setCreatedDate(task.getMadeDate());
				promoRepository.save(data);
				imageService.add(data.getId(), data.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				data.setLastModifiedBy(task.getMaker());
				data.setLastModifiedDate(task.getMadeDate());
				data.setPending(false);
				promoRepository.save(data);
				imageService.update(data.getId(), data.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoRepository.delete(data);
				imageService.delete(data.getId());
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			promoRepository.save(false, data.getId());
		}

		taskService.confirm(task);
		return data.getTitle();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Task task, String error) {
		try {
			Promo data = TaskUtil.getObject(task, Promo.class);
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				promoRepository.save(false, data.getId());
			}

			taskService.save(task, error);
			return error;
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

}
