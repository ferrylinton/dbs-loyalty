package com.dbs.loyalty.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CachingService {
	
	private final CacheManager cacheManager;
	 
	public void evict(String cacheName, String cacheKey) {
	    cacheManager.getCache(cacheName).evict(cacheKey);
	}
	 
	public void evict(String cacheName) {
	    cacheManager.getCache(cacheName).clear();
	}
	
	public void evict() {
	    cacheManager.getCacheNames()
	    	.stream()
	    	.forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}
	
}
