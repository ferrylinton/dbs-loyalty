package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="FeedbackAnswer", description = "Feedback Answer's data")
@Setter
@Getter
public class FeedbackAnswerDto implements Comparable<FeedbackAnswerDto> {

	@ApiModelProperty(value = "FeedbackAnswer id", example = "646e8a2a-4ca4-459a-9da8-2a31daaecd38", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Feedback's question number", example = "1", required = true, position = 1)
	private Integer questionNumber;
	
	@ApiModelProperty(value = "Feedback's question", example = "Please rate your overall level of statisfaction with our event?", required = true, position = 2)
	private String questionText;
	
	@ApiModelProperty(value = "Feedback's answer", example = "Satisfied", required = true, position = 3)
	private String questionAnswer;

	@Override
	public int compareTo(FeedbackAnswerDto obj) {
		return (this.getQuestionNumber() - obj.getQuestionNumber());
	}
	
}
