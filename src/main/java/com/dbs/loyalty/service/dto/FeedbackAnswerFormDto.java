package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="FeedbackAnswerInput", description = "Feedback Answer Input")
@Setter
@Getter
public class FeedbackAnswerFormDto {

	@ApiModelProperty(value = "Feedback's question number", example = "1", required = true, position = 0)
	@NotNull(message = "{validation.notnull.questionNumber}")
	@Min(value = 1, message = "{validation.min.questionNumber}")
    @Max(value = 50, message = "{validation.max.questionNumber}")
	private Integer questionNumber;
	
	@ApiModelProperty(value = "Feedback's answer", example = "Satisfied", required = true, position = 1)
	private String questionAnswer;
	
}
