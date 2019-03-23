package com.dbs.loyalty.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class AuthorityDto {

	@NonNull
	private String id;
	
	@NonNull
	private String name;
	
	@Override
	public String toString() {
		return id + "," + name;
	}
	
}
