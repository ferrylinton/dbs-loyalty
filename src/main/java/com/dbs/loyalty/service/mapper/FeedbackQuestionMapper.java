package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.service.dto.FeedbackQuestionDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class FeedbackQuestionMapper {

	public abstract FeedbackQuestionDto toDto(FeedbackQuestion question);
	
}
