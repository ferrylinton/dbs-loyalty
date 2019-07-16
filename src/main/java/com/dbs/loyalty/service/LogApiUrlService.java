package com.dbs.loyalty.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.CachingConstant;
import com.dbs.loyalty.domain.LogApiUrl;
import com.dbs.loyalty.repository.LogApiUrlRepository;
import com.dbs.loyalty.service.specification.LogApiUrlSpec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogApiUrlService {

	private final LogApiUrlRepository logApiUrlRepository;

	public Optional<LogApiUrl> findById(String id){
		return logApiUrlRepository.findById(id);
	}
	
	public Optional<LogApiUrl> findByUrl(String url) {
		return logApiUrlRepository.findByUrlIgnoreCase(url);
	}
	
	@Cacheable(CachingConstant.LOG_API_URLS)
	public List<String> logApiUrls() {
		List<String> list = new ArrayList<String>();

		List<LogApiUrl> logApiUrls = logApiUrlRepository.findAll();

		for (LogApiUrl logApiUrl : logApiUrls) {
			list.add(logApiUrl.getUrl());
		}

		return list;
	}

	public Page<LogApiUrl> findAll(Map<String, String> params, Pageable pageable) {
		return logApiUrlRepository.findAll(new LogApiUrlSpec(params), pageable);
	}

	public boolean isUrlExist(String id, String url) {
		Optional<LogApiUrl> logApiUrl = logApiUrlRepository.findByUrlIgnoreCase(url);

		if (logApiUrl.isPresent()) {
			return (id == null) || (!id.equals(logApiUrl.get().getId()));
		}else {
			return false;
		}
	}

	public LogApiUrl save(LogApiUrl logApiUrl) {
		return logApiUrlRepository.save(logApiUrl);
	}
	
}