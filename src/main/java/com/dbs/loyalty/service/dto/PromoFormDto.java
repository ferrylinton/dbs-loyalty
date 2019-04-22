package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PromoFormDto extends PromoDto {

	@NotNull(message = "{validation.notnull.termAndCondition}")
    @Size(min=2, max = 50000, message = "{validation.size.termAndCondition}")
	private String termAndCondition;

	@NotNull(message = "{validation.notnull.startPeriod}")
	private Date startPeriod;

	@NotNull(message = "{validation.notnull.endPeriod}")
	private Date endPeriod;

	private boolean showInCarousel = false;

	private boolean activated = true;
	
	private String fileImageId;
	
	@JsonIgnore
    private MultipartFile imageFile;

	private PromoCategoryDto promoCategory;
	
}
