package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.dbs.loyalty.domain.TrxOrder;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.TrxOrderService;
import com.dbs.loyalty.service.TrxProductService;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.rest.TrxOrderValidator;

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
@RequestMapping("/api/trx-orders")
public class TrxOrderRestController {
	
	public static final String ADD_TRX_ORDER = "AddTrxOrder";

	private final TrxProductService trxProductService;
	
	private final TrxOrderService trxOrderService;

	@ApiOperation(
			value=ADD_TRX_ORDER, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={ @ApiResponse(code=200, message="OK", response=Response.class)})
	@EnableLogAuditCustomer(operation=ADD_TRX_ORDER)
	@PostMapping
    public TrxOrder addDeparture(@Valid @RequestBody TrxOrder trxOrder, HttpServletRequest request, HttpServletResponse response) throws BadRequestException{
		return trxOrderService.save(trxOrder);
    }
    
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new TrxOrderValidator(trxProductService));
	}
	
}
