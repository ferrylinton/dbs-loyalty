package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.CustomerImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.CustomerImageRepository;
import com.dbs.loyalty.service.dto.CustomerImageDto;
import com.dbs.loyalty.service.mapper.CustomerImageMapper;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerImageService{

	private final CustomerImageRepository customerImageRepository;
	
	private final CustomerImageMapper customerImageMapper;

	public Optional<CustomerImageDto> findByEmail(String email){
		return customerImageRepository
				.findById(email)
				.map(customerImageMapper::toDto);
	}
	
	public CustomerImageDto save(MultipartFile file) throws NotFoundException, IOException {
		Optional<CustomerImage> current = customerImageRepository.findByCustomerEmail(SecurityUtil.getLogged());
		
		if(current.isPresent()) {
			CustomerImage customerImage = ImageUtil.getImage(file, current.get());
			customerImage.setLastModifiedBy(SecurityUtil.getLogged());
			customerImage.setLastModifiedDate(Instant.now());
			customerImage = customerImageRepository.save(customerImage);
			return customerImageMapper.toDto(customerImage);
		}else {
			String message = MessageService.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}

	}
	
}
