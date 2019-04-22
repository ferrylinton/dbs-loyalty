package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="Event", description = "Event's data")
@Setter
@Getter
public class EventViewDto extends EventDto {
	
	@ApiModelProperty(value = "Event's time", example = "8:00 am", position = 6)
	private String timePeriod;
	
	@ApiModelProperty(value = "Event's image url", example = "/api/events/{id}/image", position = 8)
	private String imageUrl;
	
	@ApiModelProperty(value = "Event's material url", example = "/api/events/{id}/material", position = 9)
	private String materialUrl;
	
}
