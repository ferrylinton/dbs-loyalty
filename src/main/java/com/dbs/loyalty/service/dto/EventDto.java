package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventDto {

	@ApiModelProperty(value = "Event's id", example = "In1IverC", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Event's title", example = "Nilai Tukar Kompetitif", required = true, position = 1)
	@NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
	private String title;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 2)
	@NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
	private String description;
	
	@ApiModelProperty(value = "Event's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 3)
	@NotNull(message = "{validation.notnull.content}")
    @Size(min=2, max = 50000, message = "{validation.size.content}")
	private String content;
	
	@ApiModelProperty(value = "Event's start period date", example = "01-04-2019", required = true, position = 4)
	@NotNull(message = "{validation.notnull.startPeriod}")
	private Date startPeriod;

	@ApiModelProperty(value = "Event's end period date", example = "02-04-2019", required = true, position = 5)
	@NotNull(message = "{validation.notnull.endPeriod}")
	private Date endPeriod;

	@ApiModelProperty(value = "Event's place", example = "02-04-2019", required = true, position = 7)
	@NotNull(message = "{validation.notnull.place}")
	private String place;
	
}
