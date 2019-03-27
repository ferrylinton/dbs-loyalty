package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.dto.PromoDto;

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

	@ApiOperation(nickname="getAllByPromoCategoryId", value="getAllByPromoCategoryId", notes="Get Promos by Promo Category's Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoDto.class)})
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
    
}
