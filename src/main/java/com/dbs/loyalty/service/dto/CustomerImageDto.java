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
public class CustomerImageDto extends AbstractImageDto{

	@NonNull
	private String email;

}
