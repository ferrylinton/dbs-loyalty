package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.CustomerPromoService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.CarouselDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.mapper.PromoMapper;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.response.SuccessResponse;

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

	private final ImageService fileImageService;
	
	private final PromoService promoService;
	
	private final CustomerPromoService customerPromoService;
	
	private final PromoMapper promoMapper;

	@ApiOperation(
			nickname="GetAllPromoInCarousel", 
			value="GetAllPromoInCarousel", 
			notes="Get All Promos to be shown in Carousel. Total promos to be shown is 3",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = CarouselDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/carousel")
    public ResponseEntity<List<CarouselDto>> getAllPromoInCarousel(){
		List<CarouselDto> promos = promoService
				.findPromoInCarousel()
				.stream()
				.map(promo -> promoMapper.toCarouselDto(promo))
				.collect(Collectors.toList());
		
    	return ResponseEntity
    			.ok()
    			.body(promos);
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
    		@ApiParam(name = "promoCategoryId", value = "Promo Category Id", example = "0073fbf2-bde7-4405-9c0d-588d2bf12017")
    		@PathVariable String promoCategoryId){
    	
		List<PromoDto> promos = promoService
				.findByPromoCategoryId(promoCategoryId)
				.stream()
				.map(promo -> promoMapper.toDto(promo))
				.collect(Collectors.toList());
		
    	return ResponseEntity
    			.ok()
    			.body(promos);
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
    	
		Optional<PromoDto> current = promoService
				.findById(id)
				.map(promo -> promoMapper.toDto(promo));
    	
    	if(current.isPresent()) {
    		return ResponseEntity
    				.ok()
    				.body(current.get());
    	}else {
    		String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
    		throw new NotFoundException(message);
    	}
    }
    
	@ApiOperation(
			nickname="GetPromoImageById", 
			value="GetPromoImageById", 
			notes="Get Promo Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promos/{id}/image")
    public ResponseEntity<byte[]> getImageById(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
    	Optional<FileImage> fileImage = fileImageService.findOneByPromoId(id);
    	
    	if(fileImage.isPresent()) {
    		HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(fileImage.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(fileImage.get().getBytes());
    	}else {
    		String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
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
    	
		Optional<String> current = promoService.findTermAndConditionById(id);
    	
    	if(current.isPresent()) {
    		HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.TEXT_PLAIN);
			
    		return ResponseEntity
    				.ok()
    				.headers(headers)
    				.body(current.get());
    	}else {
    		String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
    		throw new NotFoundException(message);
    	}
    }
	
	@ApiOperation(
			nickname="AddPromoToInterested", 
			value="AddPromoToInterested", 
			notes="Add Promo to Customer's interested list",
			produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/promos/{id}")
    public ResponseEntity<AbstractResponse> addToInterested(
    		@ApiParam(name = "id", value = "Promo Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
    	customerPromoService.save(id);
    	String message = MessageUtil.getMessage(MessageConstant.SUCCESS);
    	return ResponseEntity
				.ok()
				.body(new SuccessResponse(message, null));
    }
}
