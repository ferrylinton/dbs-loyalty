package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.service.dto.FeedbackDto;

@Mapper(componentModel = "spring", uses = FeedbackQuestionMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class FeedbackMapper {

	public abstract FeedbackDto toDto(Feedback feedback);
	
}
