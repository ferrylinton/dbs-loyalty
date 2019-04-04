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
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoRepository;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.dto.PromoViewDto;
import com.dbs.loyalty.service.dto.TaskDto;
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
	
	private final UrlService urlService;

	public Optional<PromoDto> findById(String id){
		return promoRepository.findById(id).map(promo -> promoMapper.toDto(promo));
	}
	
	public Optional<PromoViewDto> findViewById(String id){
		return promoRepository.findById(id).map(promo -> promoMapper.toViewDto(promo, urlService));
	}

	public Page<PromoDto> findAll(Pageable pageable, HttpServletRequest request) {
		return promoRepository.findAll(PromoSpecification.getSpec(request), pageable)
				.map(promo -> promoMapper.toDto(promo));
	}

	public List<PromoViewDto> findPromoInCarousel(){
		return promoRepository.findPromoInCarousel()
				.stream()
				.map(promo -> promoMapper.toViewDto(promo, urlService))
				.collect(Collectors.toList());
	}
	
	public List<PromoViewDto> findByPromoCategoryId(String promoCategoryId){
		return promoRepository.findByPromoCategoryId(promoCategoryId)
				.stream()
				.map(promo -> promoMapper.toViewDto(promo, urlService))
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

	public String execute(TaskDto taskDto) throws IOException {
		PromoDto promoDto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), PromoDto.class);
		
		if(taskDto.isVerified()) {
			Promo promo = promoMapper.toEntity(promoDto);
			
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				promo.setCreatedBy(taskDto.getMaker());
				promo.setCreatedDate(taskDto.getMadeDate());
				promoRepository.save(promo);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				promo.setLastModifiedBy(taskDto.getMaker());
				promo.setLastModifiedDate(taskDto.getMadeDate());
				promoRepository.save(promo);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				promoRepository.delete(promo);
			}
		}

		return promoDto.getTitle();
	}
	
}
