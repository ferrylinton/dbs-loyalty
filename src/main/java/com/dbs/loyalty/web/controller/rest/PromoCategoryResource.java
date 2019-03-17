package com.dbs.loyalty.web.controller.rest;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * REST controller for managing PromoCategory.
 */
@RestController
@RequestMapping("/api")
public class PromoCategoryResource {

	private final String SORT_BY = "name";
	
    private final PromoCategoryService promoCategoryService;

    public PromoCategoryResource(PromoCategoryService promoCategoryService) {
        this.promoCategoryService = promoCategoryService;
    }

    /**
     * GET  /promo-categories : get all the promoCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of promoCategories in body
     */
    @ApiOperation(value = "", authorizations = { @Authorization(value="JWT") })
    @GetMapping("/promo-categories")
    public ResponseEntity<List<PromoCategory>> getAllPromoCategories() {
        List<PromoCategory> promoCategories = promoCategoryService.findAll(Sort.by(SORT_BY));
        return ResponseEntity.ok().body(promoCategories);
    }

    
}
