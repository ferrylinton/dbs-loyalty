package com.dbs.loyalty.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackAnswerDto implements Comparable<FeedbackAnswerDto> {

	private String id;
	
	private Integer questionNumber;
	
	private String questionText;
	
	private String questionAnswer;

	@Override
	public int compareTo(FeedbackAnswerDto obj) {
		return (this.getQuestionNumber() - obj.getQuestionNumber());
	}
	
}
