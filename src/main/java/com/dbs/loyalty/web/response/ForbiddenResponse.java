package com.dbs.loyalty.web.response;

import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.util.IpUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Forbidden Response
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="ForbiddenResponse", description = "Forbidden Response")
@Setter
@Getter
public class ForbiddenResponse {

	@ApiModelProperty(value = "Request URL", example = "htt://localhost:818/loyalty/api/airports", position = 0)
	private String url;
	
	@ApiModelProperty(value = "Forbidden Message", example = "Forbidden", position = 1)
	private String message;
	
	public ForbiddenResponse(HttpServletRequest request) {
		url = IpUtil.getPrefixUrl(request) + request.getRequestURI();
    	message = Constant.FORBIDDEN;
	}
}
