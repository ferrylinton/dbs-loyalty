package com.dbs.loyalty.service.dto;

import java.util.List;

import com.dbs.loyalty.domain.enumeration.FormType;
import com.dbs.loyalty.model.Pair;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackQuestionDto implements Comparable<FeedbackQuestionDto> {

	private String id;
	
	private Integer questionNumber;
	
	private String questionText;
	
	private FormType formType;

	private List<Pair<String, String>> questionOptions;
	
	@Override
	public int compareTo(FeedbackQuestionDto obj) {
		return (this.getQuestionNumber() - obj.getQuestionNumber());
	}
	
}
