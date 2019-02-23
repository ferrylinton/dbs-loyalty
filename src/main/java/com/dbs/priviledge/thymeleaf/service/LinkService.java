package com.dbs.priviledge.thymeleaf.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.service.MessageService;

@Service
public class LinkService {

	private final String DISABLED 	= " disabled";
	
	private final String DESC_ICON	= "<i class='text-primary icon-down'></i>";
	
	private final String ASC_ICON	= "<i class='text-primary icon-up'></i>";

	private HttpServletRequest request;

	public LinkService(HttpServletRequest request) {
		this.request = request;
	}
	
	public String sortLink(String field){
		Page<?> page = (Page<?>) request.getAttribute(Constant.PAGE);
		List<Order> orders = page.getSort().stream().collect(Collectors.toList());
		
		if(page.getTotalElements() > 0) {
			return String.format("<a class='text-secondary' href='%s'>%s</a>", getSortUrl(field, page.getNumber(), orders), getLabel(field, orders));
		}else {
			return getLabel(field);
		}
	}
	
	private String getLabel(String field) {
		return MessageService.getMessage(String.format("label.%s", field));
	}
	
	private String getSortUrl(String field, int pageNumber, List<Order> orders) {
		if(orders.size() > 0) {
			if(field.equals(orders.get(0).getProperty()) && orders.get(0).getDirection() == Direction.ASC) {
				return getUrl(pageNumber, field, Direction.DESC);
			}
		}
		
		return getUrl(pageNumber, field, Direction.ASC);
	}
	
	private String getLabel(String field, List<Order> orders) {
		if(orders.size() > 0 && field.equals(orders.get(0).getProperty())) {
			if(orders.get(0).getDirection() == Direction.DESC) {
				return String.format("%s %s", getLabel(field), DESC_ICON);
			}else {
				return String.format("%s %s", getLabel(field), ASC_ICON);
			}
		}
		
		return getLabel(field);
	}
	
	public String nextLink(Page<?> page) {
		String btnClass = page.hasNext() ? Constant.EMPTY : DISABLED;
		return String.format("<a class='btn btn-sm btn-secondary %s' href='%s'>" + getLabel("next") + "<i class='icon-right-open'></i></a>", btnClass, getNextUrl(page));
	}
	
	public String previousLink(Page<?> page) {
		String btnClass = page.hasPrevious() ? Constant.EMPTY : DISABLED;
		return String.format("<a class='btn btn-sm btn-secondary %s' href='%s'><i class='icon-left-open'></i>"  + getLabel("previous") +  "</a>", btnClass, getPreviousUrl(page));
	}
	
	public String getNextUrl(Page<?> page) {
		int pageNumber = page.hasNext() ? page.getNumber() + 1 : page.getNumber();
		return getUrl(page, pageNumber);
	}
	
	public String getPreviousUrl(Page<?> page) {
		int pageNumber = page.hasPrevious() ? page.getNumber() - 1 : page.getNumber();
		return getUrl(page, pageNumber);
	}
	
	private String getUrl(Page<?> page, int pageNumber) {
		List<Order> orders = page.getSort().stream().collect(Collectors.toList());
		
		if(orders.size() > 0) {
			return getUrl(pageNumber, orders.get(0).getProperty(), orders.get(0).getDirection());
		}else {
			return getUrl(pageNumber);
		}
	}
	
	private String getUrl(int pageNumber) {
		return String.format("?%spage=%d", getKeyword(), pageNumber);
	}
	
	private String getUrl(int page, String field, Direction direction) {
		return String.format("?%spage=%d&sort=%s,%s", getKeyword(), page, field, direction.name());
	}
	
	private String getKeyword() {
		String keyword = request.getParameter(Constant.KEYWORD);
		return (keyword == null) ? Constant.EMPTY : String.format("keyword=%s&", keyword);
	}
	
}
