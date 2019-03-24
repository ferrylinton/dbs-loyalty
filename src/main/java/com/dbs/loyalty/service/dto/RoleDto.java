package com.dbs.loyalty.service.dto;

import static com.dbs.loyalty.config.constant.Constant.NAME_REGEX;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of Role DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class RoleDto extends AbstractAuditDto {

	@NonNull
	private String id;
	
	@NonNull
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = NAME_REGEX, message = "{validation.pattern.name}")
    @Size(min = 2, max = 40, message = "{validation.size.name}")
	private String name;
	
	@NotEmpty(message = "{validation.notempty.authorities}")
	private Set<AuthorityDto> authorities;
	
	@Override
	public String toString() {
		return id + "," + name;
	}
	
}
