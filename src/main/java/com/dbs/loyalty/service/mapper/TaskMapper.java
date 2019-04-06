package com.dbs.loyalty.service.mapper;

import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.service.dto.TaskDto;

@Mapper(componentModel = "spring")
public abstract class TaskMapper extends EntityMapper<TaskDto, Task> {
	
}
