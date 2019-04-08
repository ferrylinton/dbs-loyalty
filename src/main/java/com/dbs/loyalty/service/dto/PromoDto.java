package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PromoDto extends AbstractImageDto {

	private String id;
	
	@NotNull(message = "{validation.notnull.code}")
    @Size(min=2, max = 50, message = "{validation.size.code}")
	private String code;
	
	@NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
	private String title;
	
	@NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
	private String description;
	
	@NotNull(message = "{validation.notnull.termAndCondition}")
    @Size(min=2, max = 50000, message = "{validation.size.termAndCondition}")
	private String termAndCondition;

	@NotNull(message = "{validation.notnull.startPeriod}")
	private Date startPeriod;

	@NotNull(message = "{validation.notnull.endPeriod}")
	private Date endPeriod;

	private boolean showInCarousel = false;

	private boolean activated = true;

	private PromoCategoryDto promoCategory;
	
}
