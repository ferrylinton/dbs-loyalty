package com.dbs.loyalty.web.controller.rest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.AddressService;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.AddressDto;
import com.dbs.loyalty.service.dto.CountryDto;
import com.dbs.loyalty.service.mapper.AddressMapper;
import com.dbs.loyalty.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Address API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { SwaggerConstant.ADDRESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {
	
	private static final String GET_ADDRESSES = "GetAddresses";
	
	private static final String ADD_ADDRESS = "AddAddress";

    private final AddressService addressService;
    
    private final CityService cityService;
    
    private final CustomerService customerService;
    
    private final AddressMapper addressMapper;

    /**
     * GET  /api/addresses : Get addresses
     *
     * @return list of countries
     */
    @ApiOperation(
    		nickname=GET_ADDRESSES, 
    		value=GET_ADDRESSES, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=CountryDto.class, responseContainer="List")})
    @LogAuditApi(name=GET_ADDRESSES)
    @GetMapping
    public List<AddressDto> getAddresses() {
    	return addressMapper.toDto(addressService.findByCustomerEmail(SecurityUtil.getLogged()));
    }
    
    /**
     * POST  /api/addresses : Add addresses
     *
     * @return response
     */
    @ApiOperation(
    		nickname=ADD_ADDRESS, 
    		value=ADD_ADDRESS, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response=AddressDto.class)})
    @LogAuditApi(name=ADD_ADDRESS, saveRequest=true, saveResponse=true)
    @PostMapping
    public AddressDto save(@Valid @RequestBody AddressDto addressDto) throws BadRequestException{
    	Optional<Customer> customer = customerService.findByEmail(SecurityUtil.getLogged());
    	
    	Address address = addressMapper.toEntity(addressDto, cityService);
    	address.setCustomer(customer.get());
    	address.setCreatedBy(SecurityUtil.getLogged());
    	address.setCreatedDate(Instant.now());
    	
    	address = addressService.save(address);
    	return addressMapper.toDto(address);
    }

}
