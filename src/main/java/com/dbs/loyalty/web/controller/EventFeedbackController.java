package com.dbs.loyalty.web.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.evt.FeedbackCustomer;
import com.dbs.loyalty.service.FeedbackCustomerService;
import com.dbs.loyalty.service.FeedbackQuestionService;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventFeedbackController {

	private static final String REDIRECT 	= "redirect:/event/%s/feedback";
	
	private static final String VIEW 		= "eventfeedback/eventfeedback-view";
	
	private static final String SORT_BY 	= "customer.name";
	
	private final FeedbackQuestionService feedbackQuestionService;
	
	private final FeedbackCustomerService feedbackCustomerService;
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{eventId}/feedback")
	public String viewEventFeedback(
			@RequestParam Map<String, String> params, 
			@PathVariable String eventId, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<FeedbackCustomer> page = feedbackCustomerService.findFeedbackCustomers(eventId, params, PageUtil.getPageable(params, order));
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return String.format(REDIRECT, eventId);
		}else {
			model.addAttribute("eventId", eventId);
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			setQuestions(eventId, model);
			setHeaders(page, model);
			return VIEW;
		}
	}
	
	private void setHeaders(Page<FeedbackCustomer> page, Model model) {
		if(page.hasContent()) {
			model.addAttribute("answers", page.getContent().get(0).getAnswers());
		}
	}
	
	private void setQuestions(String eventId, Model model) {
		model.addAttribute("questions", feedbackQuestionService.findByEventId(eventId));
	}
	
}
