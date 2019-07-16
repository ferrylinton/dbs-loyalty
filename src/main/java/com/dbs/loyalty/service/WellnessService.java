package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Wellness;
import com.dbs.loyalty.repository.WellnessRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WellnessService {

	private final WellnessRepository wellnessRepository;
	
	public Optional<Wellness> findById() {
		return wellnessRepository.findById(SecurityUtil.getId());
	}
	
}
