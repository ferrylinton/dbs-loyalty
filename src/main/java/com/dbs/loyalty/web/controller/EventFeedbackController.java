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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.service.FeedbackCustomerService;
import com.dbs.loyalty.service.FeedbackQuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventFeedbackController extends AbstractPageController {

	private final FeedbackQuestionService feedbackQuestionService;
	
	private final FeedbackCustomerService feedbackCustomerService;
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{eventId}/feedback")
	public String viewEventFeedback(@RequestParam Map<String, String> params, @PathVariable String eventId, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "customer.name");
		Page<FeedbackCustomer> page = feedbackCustomerService.findWithCustomerAndAnswersByEventId(eventId, getPageable(params, order));
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/event" + eventId + "/feedback";
		}else {
			request.setAttribute(PAGE, page);
			setQuestions(eventId, request);
			setHeaders(page, request);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "event/event-feedback";
		}
	}
	
	private void setHeaders(Page<FeedbackCustomer> page, HttpServletRequest request) {
		if(page.hasContent()) {
			request.setAttribute("answers", page.getContent().get(0).getAnswers());
		}
	}
	
	private void setQuestions(String eventId, HttpServletRequest request) {
		request.setAttribute("questions", feedbackQuestionService.findByEventId(eventId));
	}
	
}
