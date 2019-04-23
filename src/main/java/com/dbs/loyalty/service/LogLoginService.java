package com.dbs.loyalty.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.repository.LogLoginRepository;
import com.dbs.loyalty.service.specification.LogLoginSpecification;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogLoginService {

	private final LogLoginRepository logLoginRepository;

	public LogLogin save(LogLogin logLogin) {
		return logLoginRepository.save(logLogin);
	}
	
	public Page<LogLogin> findAll(Pageable pageable, HttpServletRequest request) {
		return logLoginRepository.findAll(LogLoginSpecification.getSpec(request), pageable);
	}
	
	public LogLogin getLastLogin() {
		List<LogLogin> logLogins = logLoginRepository.findTop2ByUsernameOrderByCreatedDateDesc(SecurityUtil.getLogged());
		
		if(logLogins.size() > 1) {
			return logLogins.get(1);
		}else if(logLogins.size() == 1) {
			return logLogins.get(0);
		}else {
			return null;
		}
	}
}
