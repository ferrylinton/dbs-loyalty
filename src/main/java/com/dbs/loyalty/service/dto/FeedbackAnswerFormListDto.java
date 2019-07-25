package com.dbs.loyalty.service.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="FeedbackAnswerInputList", description = "Feedback Answer Input List")
@Setter
@Getter
public class FeedbackAnswerFormListDto {

	@JsonProperty("answers")
	@Valid
	@NotEmpty
	private List<FeedbackAnswerFormDto> feedbackAnswerFormDtos;

	
}
