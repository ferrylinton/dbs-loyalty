package com.dbs.loyalty.service.dto;

import java.time.Instant;

import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of Task
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class TaskDto {

	@NonNull
	private String id;
	
	private TaskOperation taskOperation;
	
	private TaskStatus taskStatus;
	
	private String taskDataType;
	
	private String taskDataId;
	
	private String taskDataOld;
	
	private String taskDataNew;
	
	private String maker;
	
	private Instant madeDate;
	
	private String checker;
	
	private Instant checkedDate;
	
	private String message;
	
	private String error;
	
	private String errorDetail;
	
	private boolean verified = false;
	
}
