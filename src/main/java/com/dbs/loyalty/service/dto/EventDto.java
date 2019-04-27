package com.dbs.loyalty.service.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="Event", description = "Event's data")
@Setter
@Getter
public class EventDto {

	@ApiModelProperty(value = "Event's id", example = "646e8a2a-4ca4-459a-9da8-2a31daaecd38", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Event's title", example = "Nilai Tukar Kompetitif", required = true, position = 1)
	private String title;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 2)
	private String description;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 3)
	private String content;
	
	@ApiModelProperty(value = "Event's start period date", example = "01-04-2019", required = true, position = 4)
	private Date startPeriod;

	@ApiModelProperty(value = "Event's end period date", example = "02-04-2019", required = true, position = 5)
	private Date endPeriod;
	
	@ApiModelProperty(value = "Event's time", example = "8:00 am", position = 6)
	private String timePeriod;

	@ApiModelProperty(value = "Event's place", example = "02-04-2019", required = true, position = 7)
	private String place;
	
	@ApiModelProperty(value = "Event's image url", example = "/api/events/{id}/image", position = 8)
	private String imageUrl;
	
	@ApiModelProperty(value = "Event's material url", example = "/api/events/{id}/material", position = 9)
	private String materialUrl;
	
}
