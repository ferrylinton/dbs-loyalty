package com.dbs.loyalty.service.dto;

import java.util.List;

import com.dbs.loyalty.domain.enumeration.FormType;
import com.dbs.loyalty.model.Pair;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="FeedbackQuestion", description = "Feedback Question")
@Setter
@Getter
public class FeedbackQuestionDto implements Comparable<FeedbackQuestionDto> {

	@ApiModelProperty(value = "Feedback Question id", example = "77UTTDWJX3zNWABg9ixZX9", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Feedback's question number", example = "1", required = true, position = 1)
	private Integer questionNumber;
	
	@ApiModelProperty(value = "Feedback's question", example = "Please rate your overall level of statisfaction with our event?", required = true, position = 2)
	private String questionText;
	
	@ApiModelProperty(value = "Feedback's type of question", example = "RADIOBUTTON, TEXTAREA", required = true, position = 3)
	private FormType formType;

	@ApiModelProperty(value = "Feedback's options value if type of question if RADIOBUTTON", example = "{ 'key': 'a', 'value': 'Very Dissatisfied' }, { 'key': 'b', 'value': 'Dissatisfied' }", required = true, position = 4)
	private List<Pair<String, String>> questionOptions;
	
	@Override
	public int compareTo(FeedbackQuestionDto obj) {
		return (this.getQuestionNumber() - obj.getQuestionNumber());
	}
	
}
