package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.PromoCategoryRepository;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.PromoCategoryMapper;
import com.dbs.loyalty.service.specification.PromoCategorySpecification;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoCategoryService{
	
	private Sort sortByName = Sort.by("name");
	
	private final ObjectMapper objectMapper;
	
	private final PromoCategoryRepository promoCategoryRepository;
	
	private final PromoCategoryMapper promoCategoryMapper;

	public Optional<PromoCategoryDto> findById(String id){
		return promoCategoryRepository.findById(id).map(promoCategoryMapper::toDto);
	}
	
	public List<PromoCategoryDto> findAll(){
		return promoCategoryRepository.findAll(sortByName)
				.stream()
				.map(promoCategoryMapper::toDto)
				.collect(Collectors.toList());
	}

	public Page<PromoCategoryDto> findAll(Pageable pageable, HttpServletRequest request) {
		return promoCategoryRepository.findAll(PromoCategorySpecification.getSpec(request), pageable)
				.map(promoCategoryMapper::toDto);
	}
	
	public Optional<PromoCategoryDto> findByName(String name) {
		return promoCategoryRepository.findByNameIgnoreCase(name).map(promoCategoryMapper::toDto);
	}

	public boolean isNameExist(PromoCategoryDto promoCategoryDto) {
		Optional<PromoCategory> promoCategory = promoCategoryRepository.findByNameIgnoreCase(promoCategoryDto.getName());

		if (promoCategory.isPresent()) {
			return (promoCategoryDto.getId() == null) || (!promoCategoryDto.getId().equals(promoCategory.get().getId()));
		}else {
			return false;
		}
	}

	public String execute(TaskDto taskDto) throws IOException {
		PromoCategoryDto promoCategoryDto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), PromoCategoryDto.class);
		
		if(taskDto.isVerified()) {
			PromoCategory promoCategory = promoCategoryMapper.toEntity(promoCategoryDto);
			
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				promoCategory.setCreatedBy(taskDto.getMaker());
				promoCategory.setCreatedDate(taskDto.getMadeDate());
				promoCategoryRepository.save(promoCategory);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				promoCategory.setLastModifiedBy(taskDto.getMaker());
				promoCategory.setLastModifiedDate(taskDto.getMadeDate());
				promoCategoryRepository.save(promoCategory);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				promoCategoryRepository.delete(promoCategory);
			}
		}

		return promoCategoryDto.getName();
	}
	
}
