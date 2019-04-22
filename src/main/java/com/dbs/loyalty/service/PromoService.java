package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.dto.PromoFormDto;
import com.dbs.loyalty.service.dto.PromoViewDto;
import com.dbs.loyalty.service.mapper.PromoMapper;
import com.dbs.loyalty.service.specification.PromoSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoService{

	private final PromoRepository promoRepository;
	
	private final ObjectMapper objectMapper;
	
	private final PromoMapper promoMapper;

	public Optional<PromoDto> findById(String id){
		return promoRepository.findById(id).map(promo -> promoMapper.toDto(promo));
	}
	
	public Optional<String> findTermAndConditionById(String id){
		return promoRepository.findTermAndConditionById(id);
	}
	
	public Optional<PromoViewDto> findViewById(String id){
		return promoRepository.findById(id).map(promo -> promoMapper.toViewDto(promo));
	}

	public Page<PromoDto> findAll(Pageable pageable, HttpServletRequest request) {
		return promoRepository.findAll(PromoSpecification.getSpec(request), pageable)
				.map(promo -> promoMapper.toDto(promo));
	}

	public List<CarouselDto> findPromoInCarousel(){
		List<CarouselDto> carouselDtos = promoRepository.findPromoInCarousel()
				.stream()
				.map(promo -> promoMapper.toCarouselDto(promo))
				.collect(Collectors.toList());
		
		if(carouselDtos.size() > 3) {
			return carouselDtos.subList(0, 3);
		}else {
			return carouselDtos;
		}
	}
	
	public List<PromoViewDto> findByPromoCategoryId(String promoCategoryId){
		return promoRepository.findByPromoCategoryId(promoCategoryId)
				.stream()
				.map(promo -> promoMapper.toViewDto(promo))
				.collect(Collectors.toList());
	}
	
	public boolean isCodeExist(PromoDto promoDto) {
		Optional<Promo> promo = promoRepository.findByCodeIgnoreCase(promoDto.getCode());

		if (promo.isPresent()) {
			return (promoDto.getId() == null) || (!promoDto.getId().equals(promo.get().getId()));
		}else {
			return false;
		}
	}
	
	public boolean isTitleExist(PromoDto promoDto) {
		Optional<Promo> promo = promoRepository.findByTitleIgnoreCase(promoDto.getTitle());

		if (promo.isPresent()) {
			return (promoDto.getId() == null) || (!promoDto.getId().equals(promo.get().getId()));
		}else {
			return false;
		}
	}

	public String execute(Task task) throws IOException {
		PromoFormDto promoDto = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), PromoFormDto.class);
		
		if(task.getVerified()) {
			Promo promo = promoMapper.toEntity(promoDto);
			
			if(task.getTaskOperation() == TaskOperation.ADD) {
				promo.setCreatedBy(task.getMaker());
				promo.setCreatedDate(task.getMadeDate());
				promoRepository.save(promo);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				promo.setLastModifiedBy(task.getMaker());
				promo.setLastModifiedDate(task.getMadeDate());
				promoRepository.save(promo);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				promoRepository.delete(promo);
			}
		}

		return promoDto.getTitle();
	}
	
}
