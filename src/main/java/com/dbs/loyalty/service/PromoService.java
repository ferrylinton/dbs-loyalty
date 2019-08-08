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
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.specification.PromoSpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoService{

	private final PromoRepository promoRepository;
	
	private final ImageService imageService;
	
	private final ObjectMapper objectMapper;
	
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
	public void taskSave(Promo newPromo) throws IOException {
		if(newPromo.getId() == null) {
			FileImageTask fileImageTask = imageService.add(newPromo.getMultipartFileImage());
			newPromo.setImage(fileImageTask.getId());
			
			taskService.saveTaskAdd(DomainConstant.ROLE, toString(newPromo));
		}else {
			Promo oldPromo = promoRepository.getOne(newPromo.getId());
			
			if(newPromo.getMultipartFileImage().isEmpty()) {
				newPromo.setImage(newPromo.getId());
			}else {
				FileImageTask fileImageTask = imageService.add(newPromo.getMultipartFileImage());
				newPromo.setImage(fileImageTask.getId());
			}
			
			oldPromo.setImage(newPromo.getId());
			
			promoRepository.save(true, newPromo.getId());
			taskService.saveTaskModify(DomainConstant.ROLE, toString(oldPromo), toString(newPromo));
		}
	}

	@Transactional
	public void taskDelete(Promo promo) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.ROLE, toString(promo));
		promoRepository.save(true, promo.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		Promo promo = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Promo.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				promo.setCreatedBy(task.getMaker());
				promo.setCreatedDate(task.getMadeDate());
				promoRepository.save(promo);
				imageService.add(promo.getId(), promo.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				promo.setLastModifiedBy(task.getMaker());
				promo.setLastModifiedDate(task.getMadeDate());
				promo.setPending(false);
				promoRepository.save(promo);
				imageService.update(promo.getId(), promo.getImage(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoRepository.delete(promo);
				imageService.delete(promo.getId());
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			promoRepository.save(false, promo.getId());
		}

		return promo.getTitle();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Exception ex, Task task) {
		try {
			Promo promo = toPromo((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				promoRepository.save(false, promo.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	private String toString(Promo promo) throws JsonProcessingException {
		return objectMapper.writeValueAsString(promo);
	}
	
	private Promo toPromo(String content) throws IOException {
		return objectMapper.readValue(content, Promo.class);
	}
	
}
