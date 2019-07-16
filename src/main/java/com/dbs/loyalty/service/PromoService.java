package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.specification.PromoSpec;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoService{

	private final PromoRepository promoRepository;
	
	private final ImageService imageService;
	
	private final ObjectMapper objectMapper;

	public Optional<Promo> findById(String id){
		return promoRepository.findById(id);
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
	
	public String execute(Task task) throws IOException {
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
	
}
