package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PromoCustomerService;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Promo Customer API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { PROMO })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/promos")
public class PromoCustomerRestController {

	public static final String ADD_TO_INTERESTED_IN_PROMO =  "AddToInterestedInPromo";
	
	private final PromoCustomerService customerPromoService;

	@ApiOperation(
			value=ADD_TO_INTERESTED_IN_PROMO, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=Response.class)})
	@EnableLogAuditCustomer(operation=ADD_TO_INTERESTED_IN_PROMO)
    @PostMapping("/{id}")
    public Response addToInterestedInPromo(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id,
    		HttpServletRequest request, HttpServletResponse response) throws NotFoundException{
    	
		return customerPromoService.save(id);
    }
	
}
