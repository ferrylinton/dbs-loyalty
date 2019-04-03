package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.Base64Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { PROMO })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PromoRestController {

	private final PromoService promoService;

	@ApiOperation(
			nickname="GetAllPromoInCarousel", 
			value="GetAllPromoInCarousel", 
			notes="Get All Promos to be shown in Carousel",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/carousel")
    public ResponseEntity<List<PromoDto>> getAllPromoInCarousel(){
		List<PromoDto> promos = promoService.findPromoInCarousel();
    	return ResponseEntity.ok().body(promos);
    }
	
	@ApiOperation(
			nickname="GetAllByPromoCategoryId", 
			value="GetAllByPromoCategoryId", 
			notes="Get Promos by Promo Category's Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/promo-categories/{promoCategoryId}")
    public ResponseEntity<List<PromoDto>> getAllByPromoCategoryId(
    		@ApiParam(name = "promoCategoryId", value = "Promo Category Id", example = "zO0dDp9K")
    		@PathVariable String promoCategoryId){
    	
		List<PromoDto> promos = promoService.findByPromoCategoryId(promoCategoryId);
    	return ResponseEntity.ok().body(promos);
    }
	
	@ApiOperation(
			nickname="GetPromoById", 
			value="GetPromoById", 
			notes="Get Promo by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/{id}")
    public ResponseEntity<PromoDto> getById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<PromoDto> current = promoService.findById(id);
    	
    	if(current.isPresent()) {
    		return ResponseEntity.ok().body(current.get());
    	}else {
    		String message = MessageService.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
    		throw new NotFoundException(message);
    	}
    }
    
	@ApiOperation(
			nickname="GetPromoImageById", 
			value="GetPromoImageById", 
			notes="Get Promo Image by Id",
    		produces=MediaType.IMAGE_JPEG_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/promos/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<PromoDto> current = promoService.findById(id);
    	
    	if(current.isPresent()) {
    		return ResponseEntity.ok().body(Base64Util.getBytes(current.get().getImageString()));
    	}else {
    		String message = MessageService.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
    		throw new NotFoundException(message);
    	}
    }
	
	@ApiOperation(
			nickname="GetPromoTermById", 
			value="GetPromoTermById", 
			notes="Get Promo Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/{id}/term")
    public ResponseEntity<String> getTermAndConditionById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<PromoDto> current = promoService.findById(id);
    	
    	if(current.isPresent()) {
    		return ResponseEntity.ok().body(current.get().getTermAndCondition());
    	}else {
    		String message = MessageService.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
    		throw new NotFoundException(message);
    	}
    }
	
}
