package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="Promo", description = "Promo's data")
public class PromoDto extends AbstractAuditDto {

	@ApiModelProperty(value = "Promo's id", example = "In1IverC", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Promo's code", example = "100", required = true, position = 1)
	@NotNull(message = "{validation.notnull.code}")
    @Size(min=2, max = 50, message = "{validation.size.code}")
	private String code;
	
	@ApiModelProperty(value = "Promo's title", example = "Nilai Tukar Kompetitif", required = true, position = 3)
	@NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
	private String title;
	
	@ApiModelProperty(value = "Promo's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 4)
	@NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
	private String description;
	
	@JsonIgnore@NotNull(message = "{validation.notnull.termAndCondition}")
    @Size(min=2, max = 50000, message = "{validation.size.termAndCondition}")
	private String termAndCondition;
	
	@JsonIgnore
	private String imageString;
	
	@JsonIgnore
	private MultipartFile file;
	
	@JsonIgnore
	@NotNull(message = "{validation.notnull.startPeriod}")
	private Date startPeriod;
	
	@JsonIgnore
	@NotNull(message = "{validation.notnull.endPeriod}")
	private Date endPeriod;
	
	@JsonIgnore
	private boolean showInCarousel = false;
	
	@JsonIgnore
	private boolean activated = true;
	
	@JsonIgnore
	private PromoCategoryDto promoCategory;
	
}
