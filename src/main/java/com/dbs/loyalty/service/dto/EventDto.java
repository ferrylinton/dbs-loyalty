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
public class EventDto extends AbstractImageDto {

	private String id;
	
	@NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
	private String title;
	
	@NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
	private String description;
	
	@NotNull(message = "{validation.notnull.content}")
    @Size(min=2, max = 50000, message = "{validation.size.content}")
	private String content;

	@NotNull(message = "{validation.notnull.startPeriod}")
	private Date startPeriod;

	@NotNull(message = "{validation.notnull.endPeriod}")
	private Date endPeriod;
	
	@NotNull(message = "{validation.notnull.timePeriod}")
	private Date timePeriod;

	@NotNull(message = "{validation.notnull.place}")
	private String place;
	
	@NotNull(message = "{validation.notnull.material}")
	private byte[] materialBytes;
	
	@JsonIgnore
    private MultipartFile materialFile;
	
}
