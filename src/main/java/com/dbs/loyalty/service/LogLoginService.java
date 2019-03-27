package com.dbs.loyalty.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.repository.LogLoginRepository;
import com.dbs.loyalty.service.dto.LogLoginDto;
import com.dbs.loyalty.service.mapper.LogLoginMapper;
import com.dbs.loyalty.service.specification.LogLoginSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogLoginService {

	private final LogLoginRepository logLoginRepository;
	
	private final LogLoginMapper logLoginMapper;

	public LogLoginDto save(LogLoginDto logLoginDto) {
		LogLogin logLogin = logLoginMapper.toEntity(logLoginDto);
		return logLoginMapper.toDto(logLoginRepository.save(logLogin));
	}
	
	public Page<LogLoginDto> findAll(Pageable pageable, HttpServletRequest request) {
		return logLoginRepository.findAll(LogLoginSpecification.getSpec(request), pageable)
				.map(logLoginMapper::toDto);
	}
	
}
