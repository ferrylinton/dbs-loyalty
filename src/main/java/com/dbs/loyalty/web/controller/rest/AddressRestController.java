package com.dbs.loyalty.web.controller.rest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.AddressService;
import com.dbs.loyalty.service.CityService;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.dto.AddressDto;
import com.dbs.loyalty.service.dto.CountryDto;
import com.dbs.loyalty.service.mapper.AddressMapper;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;
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
    
    private final LogAuditCustomerService logAuditCustomerService;

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
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value = { @ApiResponse(code=200, message="OK", response=AddressDto.class)})
    @PostMapping
    public AddressDto save(@Valid @RequestBody AddressDto requestData, HttpServletRequest request){
    	String customerId = SecurityUtil.getId();
    	String url = UrlUtil.getFullUrl(request);
    	AddressDto oldData = null;
    	
    	try {
    		Optional<Customer> customer = customerService.findById(customerId);
        	Optional<Address> current = addressService.findByCustomerIdAndLabel(customerId, requestData.getLabel());
        	Address address = null;
        	
        	if(current.isPresent()) {
        		address = addressMapper.toEntity(requestData, cityService);
        		address.setId(current.get().getId());
        		address.setCustomer(customer.get());
        		address.setLastModifiedBy(SecurityUtil.getLogged());
        		address.setLastModifiedDate(Instant.now());
        	}else {
        		address = addressMapper.toEntity(requestData, cityService);
        		address.setCustomer(customer.get());
        		address.setCreatedBy(SecurityUtil.getLogged());
        		address.setCreatedDate(Instant.now());
        	}
		
        	setLabel(address);
        	address = addressService.save(address);
        	oldData = current.isPresent() ? addressMapper.toDto(current.get()) : null;
        	logAuditCustomerService.save(ADD_ADDRESS, url, requestData, oldData);
        	return addressMapper.toDto(address);
		} catch (Throwable t) {
			logAuditCustomerService.save(ADD_ADDRESS, url, requestData, t);
			throw t;
		}
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
