package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_NOT_FOUND;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.mapper.PromoMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Promo API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.PROMO })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/promos")
public class PromoRestController {
	
	public static final String GET_ALL_PROMO_IN_CAROUSEL = "GetAllPromoInCarousel";
	
	public static final String GET_ALL_BY_PROMO_CATEGORY_ID = "GetAllByPromoCategoryId";
	
	public static final String GET_PROMO_BY_ID = "GetPromoById";
	
	public static final String GET_PROMO_TERM_BY_ID = "GetPromoTermById";
	
	private final PromoService promoService;

	private final PromoMapper promoMapper;

	@ApiOperation(
			value=GET_ALL_PROMO_IN_CAROUSEL, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=CarouselDto.class)})
	@LogAuditApi(name=GET_ALL_PROMO_IN_CAROUSEL)
	@GetMapping("/carousel")
    public List<CarouselDto> getAllPromoInCarousel(){
		return promoService
				.findPromoInCarousel()
				.stream()
				.map(promo -> promoMapper.toCarouselDto(promo))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			value=GET_ALL_BY_PROMO_CATEGORY_ID, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=PromoDto.class)})
	@LogAuditApi(name=GET_ALL_BY_PROMO_CATEGORY_ID)
	@GetMapping("/promo-categories/{promoCategoryId}")
    public List<PromoDto> getAllByPromoCategoryId(
    		@ApiParam(name = "promoCategoryId", value = "Promo Category Id", example = "6nJfmxAD6GWtsehXfSkShg")
    		@PathVariable String promoCategoryId){
    	
		return promoService
				.findByPromoCategoryId(promoCategoryId)
				.stream()
				.map(promo -> promoMapper.toDto(promo))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			value=GET_PROMO_BY_ID, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=PromoDto.class)})
	@LogAuditApi(name=GET_PROMO_BY_ID)
	@GetMapping("/{id}")
    public PromoDto getPromoById(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<PromoDto> current = promoService
				.findById(id)
				.map(promo -> promoMapper.toDto(promo));
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, DomainConstant.PROMO, id));
    	}
    }
    
    @ApiOperation(
    		value=GET_PROMO_TERM_BY_ID, 
    		produces="text/plain", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=String.class)})
    @LogAuditApi(name=GET_PROMO_TERM_BY_ID)
    @GetMapping(value="/{id}/term", produces="text/plain")
    public String getTermAndConditionById(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<String> current = promoService.findTermAndConditionById(id);
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, DomainConstant.PROMO, id));
    	}
    }
	
}
