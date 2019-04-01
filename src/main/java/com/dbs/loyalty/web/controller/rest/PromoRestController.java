package com.dbs.loyalty.web.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
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
import lombok.extern.slf4j.Slf4j;

@Api(tags = { SwaggerConstant.Promo })
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class PromoRestController {

	private final PromoService promoService;

	@ApiOperation(
			nickname="GetAllByPromoCategoryId", 
			value="GetAllByPromoCategoryId", 
			notes="Get Promos by Promo Category's Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/promo-categories/{promoCategoryId}")
    public ResponseEntity<?> getAllByPromoCategoryId(
    		@ApiParam(name = "promoCategoryId", value = "Promo Category Id", example = "zO0dDp9K")
    		@PathVariable String promoCategoryId){
    	
    	try {
	    	List<PromoDto> promos = promoService.findByPromoCategoryId(promoCategoryId);
	    	return ResponseEntity.ok().body(promos);
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
	
	@ApiOperation(
			nickname="GetPromoById", 
			value="GetPromoById", 
			notes="Get Promo by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/{id}")
    public ResponseEntity<?> getById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id){
    	
    	try {
	    	Optional<PromoDto> promoDto = promoService.findById(id);
	    	return ResponseEntity.ok().body(promoDto.get());
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
    
	@ApiOperation(
			nickname="GetPromoImageById", 
			value="GetPromoImageById", 
			notes="Get Promo Image by Id",
    		produces=MediaType.IMAGE_JPEG_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/promos/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImageById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id){
    	
    	try {
	    	Optional<PromoDto> promoDto = promoService.findById(id);
	    	
	    	if(promoDto.isPresent()) {
	    		return ResponseEntity.ok().body(Base64Util.getBytes(promoDto.get().getImageString()));
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
	
	@ApiOperation(
			nickname="GetPromoTermById", 
			value="GetPromoTermById", 
			notes="Get Promo Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/{id}/term")
    public ResponseEntity<?> getTermAndConditionById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id){
    	
    	try {
	    	Optional<PromoDto> promoDto = promoService.findById(id);
	    	
	    	if(promoDto.isPresent()) {
	    		return ResponseEntity.ok().body(promoDto.get().getTermAndCondition());
	    	}else {
	    		return ResponseEntity.notFound().build();
	    	}
	    	
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }
	
}
