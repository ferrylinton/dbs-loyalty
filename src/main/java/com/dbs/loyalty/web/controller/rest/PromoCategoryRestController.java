package com.dbs.loyalty.web.controller.rest;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.web.response.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing PromoCategory.
 */
@Api(tags = { SwaggerConstant.PROMO_CATEGORY })
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class PromoCategoryRestController {

    private final PromoCategoryService promoCategoryService;

    /**
     * GET  /promo-categories : get all the promoCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of promoCategories in body
     */
    @ApiOperation(
    		nickname="GetPromoCategories", 
    		value="GetPromoCategories", 
    		notes="Get all Promo Category",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoCategoryDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promo-categories")
    public ResponseEntity<?> getPromoCategories() {
    	try {
	        List<PromoCategoryDto> promoCategoryDtos = promoCategoryService.findAll();
	        return ResponseEntity.ok().body(promoCategoryDtos);
    	} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    }

}
