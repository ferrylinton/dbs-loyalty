package com.dbs.loyalty.web.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Reward;
import com.dbs.loyalty.service.RewardService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer/reward")
public class RewardController {
	
	private static final String REDIRECT 	= "redirect:/reward/%s";
	
	private static final String VIEW 		= "reward/reward-view";
	
	private static final String DETAIL 		= "reward/reward-detail";

	private final RewardService rewardService;

	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping("/{customerId}")
	public String viewRewards(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params,
			@PathVariable String customerId,
			Sort sort, Model model) {
		
		params.put(DomainConstant.CUSTOMER, customerId);
		System.out.println("params : " + params);
		Order order = PageUtil.getOrder(sort, DomainConstant.CREATED_DATE);
		Page<Reward> page = rewardService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping("/{customerId}/{id}/detail")
	public String viewRewardDetail(ModelMap model, @PathVariable String id){
		Optional<Reward> reward = rewardService.findById(id);
		
		if (reward.isPresent()) {
			model.addAttribute(DomainConstant.REWARD, reward.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
		return DETAIL;
	}

}
