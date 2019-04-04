package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.LovedOneService;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.LovedOneAddDto;
import com.dbs.loyalty.service.dto.LovedOneDto;
import com.dbs.loyalty.service.dto.LovedOneUpdateDto;
import com.dbs.loyalty.web.controller.AbstractController;
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
@RestController
@RequestMapping("/api")
public class LovedOneRestController extends AbstractController{

    private final LovedOneService lovedOneService;
    
    @ApiOperation(
    		nickname="GetLovedOnes", 
    		value="GetLovedOnes", 
    		notes="Get Customer's loved ones",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/loved-ones")
	public ResponseEntity<List<LovedOneDto>> getLovedOnes(Principal principal){
    	List<LovedOneDto> lovedOnes = lovedOneService.findByCustomerEmail(principal.getName());
		return ResponseEntity.ok().body(lovedOnes);
	}
    
    @ApiOperation(
    		nickname="AddLovedOne", 
    		value="AddLovedOne", 
    		notes="Add new customer's loved one information",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/loved-ones")
    public ResponseEntity<LovedOneDto> addCustomer(
    		@ApiParam(name = "LovedOneData", value = "Customer's loved one new data") 
    		@Valid @RequestBody LovedOneAddDto lovedOneAddDto,
    		HttpServletRequest request)  {

    	LovedOneDto lovedOneDto = lovedOneService.add(lovedOneAddDto);
		return ResponseEntity.ok().body(lovedOneDto);
    }
    
    @ApiOperation(
    		nickname="UpdateLovedOne", 
    		value="UpdateLovedOne", 
    		notes="Update customer's loved one information",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = LovedOneDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/loved-ones")
    public ResponseEntity<LovedOneDto> updateCustomer(
    		@ApiParam(name = "LovedOneData", value = "Customer's loved one new data") 
    		@Valid @RequestBody LovedOneUpdateDto lovedOneUpdateDto,
    		HttpServletRequest request) throws NotFoundException  {

    	Optional<LovedOneDto> lovedOneDto = lovedOneService.findById(lovedOneUpdateDto.getId());
		
		if(lovedOneDto.isPresent()) {
    		return ResponseEntity.ok().body(lovedOneService.update(lovedOneUpdateDto));
		}else {
			String message = MessageService.getMessage(DATA_WITH_VALUE_NOT_FOUND, lovedOneUpdateDto.getId());
			throw new NotFoundException(message);
		}
    }
   
    @InitBinder("lovedOneAddDto")
	protected void initAddBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new LovedOneAddValidator(lovedOneService));
	}
    
    @InitBinder("lovedOneUpdateDto")
	protected void initUpdateBinder(WebDataBinder binder) {
    	binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new LovedOneUpdateValidator(lovedOneService));
	}
    
}
