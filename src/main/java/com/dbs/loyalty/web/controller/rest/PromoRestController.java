package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.TagConstant;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.service.PromoService;

import io.swagger.annotations.Api;

@Api(tags = { TagConstant.Promo })
@RestController
@RequestMapping("/api")
public class PromoRestController {

	private final PromoService promoService;

    public PromoRestController(PromoService promoService) {
        this.promoService = promoService;
    }
    
    @GetMapping("/promos/promo-categories/{promoCategoryId}")
    public ResponseEntity<?> getByPromoCategoryId(@PathVariable String promoCategoryId){
    	List<Promo> promos = promoService.findByPromoCategoryId(promoCategoryId);
    	return ResponseEntity.ok().body(promos);
    }
    
}
