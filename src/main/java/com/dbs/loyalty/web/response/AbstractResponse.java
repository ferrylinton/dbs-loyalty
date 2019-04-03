package com.dbs.loyalty.web.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class AbstractResponse {

	private final String message;

}
