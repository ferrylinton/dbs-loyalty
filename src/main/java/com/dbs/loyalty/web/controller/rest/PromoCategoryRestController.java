package com.dbs.loyalty.web.controller.rest;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing PromoCategory.
 */
@Api(tags = { SwaggerConstant.PromoCategory })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PromoCategoryRestController {

    private final PromoCategoryService promoCategoryService;

    /**
     * GET  /promo-categories : get all the promoCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of promoCategories in body
     */
    @ApiOperation(value = "", authorizations = { @Authorization(value="JWT") })
    @GetMapping("/promo-categories")
    public ResponseEntity<List<PromoCategoryDto>> getAllPromoCategories() {
        List<PromoCategoryDto> promoCategories = promoCategoryService.findAll();
        return ResponseEntity.ok().body(promoCategories);
    }

}
