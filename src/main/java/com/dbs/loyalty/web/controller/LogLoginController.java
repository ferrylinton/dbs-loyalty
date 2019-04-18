package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.PAGE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.service.LogLoginService;
import com.dbs.loyalty.service.dto.LogLoginDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/loglogin")
public class LogLoginController extends AbstractPageController {

	private final LogLoginService logLoginService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "createdDate");
		Page<LogLoginDto> page = logLoginService.findAll(getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/loglogin";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "loglogin/loglogin-view";
		}
	}

}
