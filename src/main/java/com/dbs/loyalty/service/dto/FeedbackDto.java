package com.dbs.loyalty.service.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackDto {

	private String id;
	
	private Set<FeedbackQuestionDto> questions;
	
}
