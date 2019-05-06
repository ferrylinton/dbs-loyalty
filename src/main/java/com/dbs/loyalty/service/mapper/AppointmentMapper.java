package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.service.dto.AppointmentDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AppointmentMapper{

	public abstract Appointment toEntity(AppointmentDto appointmentDto);
	
}
