package com.dbs.loyalty.web.controller.rest;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.LovedOne;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.LovedOneService;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;
import com.dbs.loyalty.service.mapper.LovedOneMapper;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.validator.LovedOneAddValidator;
import com.dbs.loyalty.web.validator.LovedOneUpdateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { SwaggerConstant.LOVED_ONE })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/loved-ones")
public class LovedOneRestController {
	
	public static final String GET_LOVED_ONES = "GetLovedOnes";
	
	public static final String ADD_LOVED_ONE = "AddLovedOne";
	
	public static final String UPDATE_LOVED_ONE = "UpdateLovedOne";
	
	public static final String ADD_BINDER_NAME = "lovedOneAddDto";
	
	public static final String UPDATE_BINDER_NAME = "lovedOneUpdateDto";

    private final LovedOneService lovedOneService;
    
    private final LovedOneMapper lovedOneMapper;

    @ApiOperation(
    		nickname=GET_LOVED_ONES, 
    		value=GET_LOVED_ONES, 
    		notes="Get Customer's loved ones",
    		produces="application/json", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class, responseContainer="List")})
    @EnableLogAuditCustomer(operation=GET_LOVED_ONES)
    @GetMapping
	public List<LovedOneDto> getLovedOnes(HttpServletRequest request, HttpServletResponse response){
    	List<LovedOneDto> lovedOnes = lovedOneService
    			.findByCustomerEmail(SecurityUtil.getLogged())
    			.stream()
				.map(lovedOne -> lovedOneMapper.toDto(lovedOne))
				.collect(Collectors.toList());;
		return lovedOnes;
	}
    
    @ApiOperation(
    		nickname=ADD_LOVED_ONE, 
    		value=ADD_LOVED_ONE, 
    		notes="Add new customer's loved one information",
    		consumes="application/json",
    		produces="application/json", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class)})
    @EnableLogAuditCustomer(operation=ADD_LOVED_ONE)
    @PostMapping
    public LovedOneDto addLovedOne(
    		@ApiParam(name = "LovedOneData", value = "Customer's loved one new data") 
    		@Valid @RequestBody LovedOneAddDto lovedOneAddDto)  {

    	return lovedOneMapper.toDto(lovedOneService.add(lovedOneAddDto));
    }
    
    @ApiOperation(
    		nickname=UPDATE_LOVED_ONE, 
    		value=UPDATE_LOVED_ONE, 
    		notes="Update customer's loved one information",
    		consumes="application/json",
    		produces="application/json", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class)})
    @PutMapping
    public LovedOneDto updateCustomer(
    		@ApiParam(name = "LovedOneData", value = "Customer's loved one new data") 
    		@Valid @RequestBody LovedOneUpdateDto requestData,
    		HttpServletRequest request,
    		HttpServletResponse response) throws NotFoundException  {

    	Optional<LovedOne> current = lovedOneService.findById(requestData.getId());
		
		if(current.isPresent()) {
			LovedOneDto lovedOneDto = lovedOneMapper.toDto(lovedOneService.update(requestData));
			request.setAttribute(Constant.OLD_DATA, lovedOneMapper.toDto(current.get()));
			return lovedOneDto;
		}else {
			String message = MessageUtil.getNotFoundMessage(requestData.getId()) ;
			throw new NotFoundException(message);
		}
    }
   
    @InitBinder(ADD_BINDER_NAME)
	protected void initAddBinder(WebDataBinder binder) {
    	binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			
		    @Override
		    public void setAsText(String text) throws IllegalArgumentException{
		      setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
		    }

		    @Override
		    public String getAsText() throws IllegalArgumentException {
		      return DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE).format((LocalDate) getValue());
		    }
		    
		});
    	
		binder.addValidators(new LovedOneAddValidator(lovedOneService));
	}
    
    @InitBinder(UPDATE_BINDER_NAME)
	protected void initUpdateBinder(WebDataBinder binder) {
    	binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			
		    @Override
		    public void setAsText(String text) throws IllegalArgumentException{
		      setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
		    }

		    @Override
		    public String getAsText() throws IllegalArgumentException {
		      return DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE).format((LocalDate) getValue());
		    }
		    
		});

		binder.addValidators(new LovedOneUpdateValidator(lovedOneService));
	}
    
}
