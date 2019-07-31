package com.dbs.loyalty.web.controller.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.mapper.PromoMapper;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.ResponseUtil;

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
	
	public static final String GET_PROMO_IMAGE_BY_ID = "GetPromoImageById";
	
	private final PromoService promoService;
	
	private final ImageService imageService;

	private final PromoMapper promoMapper;

	@ApiOperation(
			value=GET_ALL_PROMO_IN_CAROUSEL, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=CarouselDto.class)})
	@EnableLogAuditCustomer(operation=GET_ALL_PROMO_IN_CAROUSEL)
	@GetMapping("/carousel")
    public List<CarouselDto> getAllPromoInCarousel(HttpServletRequest request, HttpServletResponse response){
		return promoMapper.toCarouselDto(promoService.findPromoInCarousel());
    }
	
	@ApiOperation(
			value=GET_ALL_BY_PROMO_CATEGORY_ID, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=PromoDto.class)})
	@EnableLogAuditCustomer(operation=GET_ALL_BY_PROMO_CATEGORY_ID)
	@GetMapping("/promo-categories/{promoCategoryId}")
    public List<PromoDto> getAllByPromoCategoryId(
    		@ApiParam(name = "promoCategoryId", value = "Promo Category Id", example = "6nJfmxAD6GWtsehXfSkShg")
    		@PathVariable String promoCategoryId,
    		HttpServletRequest request, 
    		HttpServletResponse response){
    	
		return promoMapper.toDto(promoService.findByPromoCategoryId(promoCategoryId));
    }
	
	@ApiOperation(
			value=GET_PROMO_BY_ID, 
			produces="application/json", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=PromoDto.class)})
	@EnableLogAuditCustomer(operation=GET_PROMO_BY_ID)
	@GetMapping("/{id}")
    public PromoDto getPromoById(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
		Optional<Promo> promo = promoService.findById(id);
    	
    	if(promo.isPresent()) {
    		return promoMapper.toDto(promo.get()) ;
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PROMO, id);
    	}
    }
    
    @ApiOperation(
    		value=GET_PROMO_TERM_BY_ID, 
    		produces="text/plain", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=String.class)})
    @EnableLogAuditCustomer(operation=GET_PROMO_TERM_BY_ID)
    @GetMapping(value="/{id}/term", produces="text/plain")
    public String getTermAndConditionById(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
		Optional<String> current = promoService.findTermAndConditionById(id);
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PROMO, id);
    	}
    }
    
    @ApiOperation(
			value=GET_PROMO_IMAGE_BY_ID, 
			produces="image/png, image/jpeg", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=Byte.class)})
	@EnableLogAuditCustomer(operation=GET_PROMO_IMAGE_BY_ID)
	@GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageByPromoId(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
    	Optional<FileImage> fileImage = imageService.findById(id);
    	
    	if(fileImage.isPresent()) {
    		return ResponseUtil.createImageResponse(fileImage.get());
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PROMO, id);
    	}
    }
	
}
