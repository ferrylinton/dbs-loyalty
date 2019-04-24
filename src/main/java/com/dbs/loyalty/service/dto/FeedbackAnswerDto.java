package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackAnswerDto {

	@NotNull(message = "{validation.notnull.questionNumber}")
	@Min(value = 1, message = "{validation.min.questionNumber}")
    @Max(value = 50, message = "{validation.max.questionNumber}")
	private Integer questionNumber;
	
	private String questionAnswer;
	
}
