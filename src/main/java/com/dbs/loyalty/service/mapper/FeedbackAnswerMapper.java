package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.FeedbackAnswer;
import com.dbs.loyalty.service.dto.FeedbackAnswerDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class FeedbackAnswerMapper {

	public abstract FeedbackAnswerDto toDto(FeedbackAnswer feedbacAnswer);
	
}
