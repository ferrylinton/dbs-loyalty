package com.dbs.loyalty.web.controller.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.TadaOrderService;
import com.dbs.loyalty.service.dto.OrderReferenceDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Tada Order API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.COUNTRY })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/tada-orders")
public class TadaOrderRestController {
	
	private static final String GENERATE_ORDER_REFERENCE = "GenerateOrderReference";
	
    private final TadaOrderService tadaOrderService;
   

    /**
     * GET  /api/tada-orders/generate-order-reference : Generate OrderReference
     *
     * @return Order Reference
     */
    @ApiOperation(
    		nickname=GENERATE_ORDER_REFERENCE, 
    		value=GENERATE_ORDER_REFERENCE, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=OrderReferenceDto.class)})
    @GetMapping("/generate-order-reference")
    public OrderReferenceDto generateOrderReference() {
    	OrderReferenceDto orderReferenceDto = new OrderReferenceDto();
    	orderReferenceDto.setOrderReferenece(tadaOrderService.generate());
    	return orderReferenceDto;
    }
    
    
}
