package com.dbs.loyalty.web.response;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.util.IpUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Unauthorized Response
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="UnauthorizedResponse", description = "Unauthorized Response")
@Setter
@Getter
public class UnauthorizedResponse {

	@ApiModelProperty(value = "Request URL", example = "htt://localhost:818/loyalty/api/airports", position = 0)
	private String url;
	
	@ApiModelProperty(value = "Unauthorized Message", example = "Expired JWT token", position = 1)
	private String message;
	
	public UnauthorizedResponse(HttpServletRequest request) {
		url = IpUtil.getPrefixUrl(request) + request.getRequestURI();
    	message = (String) request.getAttribute(Constant.MESSAGE);
    	message = (message == null) ? Constant.UNAUTHORIZED : message;
	}
}
