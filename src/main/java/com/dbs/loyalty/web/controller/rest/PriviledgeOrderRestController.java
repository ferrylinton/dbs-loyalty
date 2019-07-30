package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.PriviledgeOrder;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.PriviledgeOrderService;
import com.dbs.loyalty.service.PriviledgeProductService;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.rest.PriviledgeOrderValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Order API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.ORDER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/priviledge-orders")
public class PriviledgeOrderRestController {

	public static final String CREATE_PRIVILEDGE_ORDER = "CreatePriviledgeOrder";

	private final PriviledgeProductService priviledgeProductService;
	
	private final PriviledgeOrderService priviledgeOrderService;

	@ApiOperation(
			value=CREATE_PRIVILEDGE_ORDER, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={ @ApiResponse(code=200, message="OK", response=Response.class)})
	@EnableLogAuditCustomer(operation=CREATE_PRIVILEDGE_ORDER)
	@PostMapping
    public PriviledgeOrder createOrder(@Valid @RequestBody PriviledgeOrder priviledgeOrder) throws BadRequestException{
		return priviledgeOrderService.save(priviledgeOrder);
    }
    
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PriviledgeOrderValidator(priviledgeProductService));
	}
	
}
