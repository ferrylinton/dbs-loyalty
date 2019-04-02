package com.dbs.loyalty.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadRequestResponse {

	private String message;
	
	private List<String> fields = new ArrayList<>();

}
