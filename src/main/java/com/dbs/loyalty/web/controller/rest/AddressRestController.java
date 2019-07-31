package com.dbs.loyalty.web.controller.rest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.AddressLabelConstant;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.AddressService;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.AddressDto;
import com.dbs.loyalty.service.dto.CountryDto;
import com.dbs.loyalty.service.mapper.AddressMapper;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.validator.rest.AddressDtoValidator;

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
	
	public static final String GET_ADDRESSES = "GetAddresses";
	
	public static final String ADD_ADDRESS = "AddAddress";

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
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value = { @ApiResponse(code=200, message="OK", response=CountryDto.class, responseContainer="List")})
    @EnableLogAuditCustomer(operation=GET_ADDRESSES)
    @GetMapping
    public List<AddressDto> getAddresses(HttpServletRequest request,HttpServletResponse response) {
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
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value = { @ApiResponse(code=200, message="OK", response=AddressDto.class)})
    @EnableLogAuditCustomer(operation=ADD_ADDRESS)
    @PostMapping
    public AddressDto save(
    		@Valid @RequestBody AddressDto addressDto, 
    		HttpServletRequest request,
    		HttpServletResponse response){
    	
    	String email = SecurityUtil.getLogged();
    	Customer customer = customerService.findLoggedUserByEmail(email);
    	Optional<Address> current = addressService.findByCustomerEmailAndLabel(email, addressDto.getLabel());
    	Address address = null;
    	
    	if(current.isPresent()) {
    		request.setAttribute(Constant.OLD_DATA, addressMapper.toDto(current.get()));
    		address = addressMapper.toEntity(addressDto, cityService);
    		address.setId(current.get().getId());
    		address.setCustomer(customer);
    		address.setLastModifiedBy(email);
    		address.setLastModifiedDate(Instant.now());
    	}else {
    		address = addressMapper.toEntity(addressDto, cityService);
    		address.setCustomer(customer);
    		address.setCreatedBy(email);
    		address.setCreatedDate(Instant.now());
    	}
	
    	setLabel(address);
    	return addressMapper.toDto(addressService.save(address));
    }
    
    private void setLabel(Address address) {
    	if(AddressLabelConstant.PRIMARY.equalsIgnoreCase(address.getLabel())){
    		address.setLabel(AddressLabelConstant.PRIMARY);
		}else {
			address.setLabel(AddressLabelConstant.SECONDARY);
		}
    }

    @InitBinder
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new AddressDtoValidator(cityService));
	}
    
}
