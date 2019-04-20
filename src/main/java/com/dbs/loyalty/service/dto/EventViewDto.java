package com.dbs.loyalty.service.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="EventView", description = "Event's data")
@Setter
@Getter
public class EventViewDto {

	@ApiModelProperty(value = "Event's id", example = "In1IverC", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Event's title", example = "Nilai Tukar Kompetitif", required = true, position = 3)
	private String title;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 4)
	private String description;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 4)
	private String content;
	
	private Date startPeriod;
	
	private Date endPeriod;
	
	private Date timePeriod;
	
	private String place;
	
	@ApiModelProperty(value = "Event's image url", example = "/api/promos/{id}/image", position = 5)
	private String imageUrl;
	
	@ApiModelProperty(value = "Event's material url", example = "/api/promos/{id}/material", position = 6)
	private String materialUrl;
	
}
