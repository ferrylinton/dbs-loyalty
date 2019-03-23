package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.Constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of Promo Category DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class PromoCategoryDto {

	@NonNull
	private String id;
	
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
    @Size(min = 2, max = 100, message = "{validation.size.name}")
	private String name;
	
	@Override
	public String toString() {
		return id + "," + name;
	}
	
}
