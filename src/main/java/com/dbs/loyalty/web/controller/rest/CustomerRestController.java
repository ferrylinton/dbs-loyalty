package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.Constant.MESSAGE;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.MessageConstant.FILE_IS_EMPTY;
import static com.dbs.loyalty.config.constant.MessageConstant.SUCCESS;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.repository.FileImageRepository;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.CustomerPasswordDto;
import com.dbs.loyalty.service.dto.CustomerUpdateDto;
import com.dbs.loyalty.service.dto.CustomerViewDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;
import com.dbs.loyalty.util.HeaderTokenUtil;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.controller.AbstractController;
import com.dbs.loyalty.web.response.BadRequestResponse;
import com.dbs.loyalty.web.validator.CustomerPasswordValidator;
import com.dbs.loyalty.web.validator.CustomerUpdateValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { CUSTOMER })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CustomerRestController extends AbstractController{
	
	private final FileImageRepository fileImageRepository;
	
    private final CustomerService customerService;

    private final RestTokenProvider restTokenProvider;
    
    private final CustomerMapper customerMapper;
    
    @ApiOperation(
    		nickname="GetCustomerInfo", 
    		value="GetCustomerInfo", 
    		notes="Get Customer Information",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = CustomerDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customers/info")
	public ResponseEntity<CustomerViewDto> getCustomerInfo() throws NotFoundException{
    	Optional<CustomerViewDto> current = customerService
    			.findByEmail(SecurityUtil.getLogged())
    			.map(customer -> customerMapper.toDto(customer));
		
		if(current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
	}
    
    @ApiOperation(
    		nickname="GetCustomerImage", 
    		value="GetCustomerImage", 
    		notes="Get Customer Image", 
    		produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customers/image")
	public ResponseEntity<byte[]> getCustomerImage() throws NotFoundException {
    	Optional<FileImage> fileImage = fileImageRepository.findOneByCustomerEmail(SecurityUtil.getLogged());
    	
		if(fileImage.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(fileImage.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(fileImage.get().getBytes());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
	}
	
	@ApiOperation(
    		nickname="UpdateCustomerImage", 
    		value="UpdateCustomerImage", 
    		notes="Update customer image",
    		consumes="multipart/form-data",
    		produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/customers/image")
    public ResponseEntity<byte[]> updateCustomerImage(
    		@ApiParam(name = "file", value = "Customer's new image") 
    		@RequestParam("file") MultipartFile file) throws NotFoundException, IOException, BadRequestException  {
    	
    	if(file.isEmpty()) {
    		throw new BadRequestException(new BadRequestResponse(MessageUtil.getMessage(FILE_IS_EMPTY)));
    	}else {
    		FileImage fileImage = customerService.updateCustomerImage(file);
        	HttpHeaders headers = new HttpHeaders();
    		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    		headers.setContentType(MediaType.valueOf(fileImage.getContentType()));
    		
    		return ResponseEntity
    				.ok()
    				.headers(headers)
    				.body(fileImage.getBytes());
    	}
    }
    
    @ApiOperation(
    		nickname="UpdateCustomer", 
    		value="UpdateCustomer", 
    		notes="Update customer information, and after update customer must use new token or relogin",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = JWTTokenDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/customers")
    public ResponseEntity<JWTTokenDto> updateCustomer(
    		@ApiParam(name = "CustomerNewData", value = "Customer's new data") 
    		@Valid @RequestBody CustomerUpdateDto customerUpdateDto,
    		HttpServletRequest request)  {
    	
    	String token = null;
    	CustomerViewDto customerViewDto = customerMapper.toDto(customerService.update(customerUpdateDto));
		String jwt = HeaderTokenUtil.resolveToken(request);
        
        if (StringUtils.hasText(jwt) && restTokenProvider.validateToken(jwt)) {
            token = restTokenProvider.createToken(customerUpdateDto.getEmail(), jwt);
        }
        
		return ResponseEntity.ok().body(new JWTTokenDto(token, customerViewDto));
    }
    
    @ApiOperation(
    		nickname="ChangePassword", 
    		value="ChangePassword", 
    		notes="Change Customer's Password",
    		consumes=MediaType.APPLICATION_JSON_VALUE,
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Pair.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/customers/change-password")
    public ResponseEntity<Pair<String, String>> changePassword(
    		@ApiParam(name = "ChangePasswordData", value = "Customer's password data") 
    		@Valid @RequestBody CustomerPasswordDto customerPasswordDto)  {
    	
    	customerService.changePassword(customerPasswordDto);
		String message = MessageUtil.getMessage(SUCCESS);
		return ResponseEntity.ok().body(new Pair<String, String>(MESSAGE, message));
    }
    
    @InitBinder("customerUpdateDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new CustomerUpdateValidator(customerService));
	}
    
    @InitBinder("customerPasswordDto")
	protected void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerPasswordValidator(customerService));
	}
    
}
