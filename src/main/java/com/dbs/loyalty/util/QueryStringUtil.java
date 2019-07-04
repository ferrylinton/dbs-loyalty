package com.dbs.loyalty.util;

import java.util.Map;

import org.springframework.data.domain.Sort.Order;

import com.dbs.loyalty.config.constant.Constant;

public class QueryStringUtil {

	public static String params(Map<String,String> params) {
		params.remove(Constant.PAGE);
		params.remove(Constant.SORT);
		
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for(Map.Entry<String, String> entry : params.entrySet()) {
			if(!Constant.EMPTY.equals(entry.getValue())) {
				if(i != 0) {
					builder.append(Constant.AND);
				}
				
				builder.append(entry.getKey()).append(Constant.EQ).append(entry.getValue());
			}
			
			i++;
		}
		String result = builder.toString();
		return result.equals(Constant.EMPTY) ? Constant.EMPTY : "&" + result;
	}

	public static String page(Order order, int page) {
		StringBuilder builder = new StringBuilder();
		builder.append(Constant.SORT).append(Constant.EQ).append(order.getProperty()).append(Constant.COMMA).append(order.getDirection().name());
		builder.append(Constant.AND);
		builder.append(Constant.PAGE).append(Constant.EQ).append(page);
		return builder.toString();
	}
	
	private QueryStringUtil() {
		// hide constructor
	}
	
}
