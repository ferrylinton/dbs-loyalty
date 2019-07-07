package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.service.DateService;
import com.dbs.loyalty.service.dto.AppointmentDto;

@Mapper(
		componentModel="spring", 
		uses={MedicalProviderMapper.class, MedicalProviderCityMapper.class, MedicalProviderBranchMapper.class, HealthPackageMapper.class}, 
		injectionStrategy=InjectionStrategy.CONSTRUCTOR)
public abstract class AppointmentMapper{

	@Mapping(source="date", target="date", dateFormat=DateService.JAVA_DATE)
	@Mapping(source="time", target="time", dateFormat=DateService.JAVA_TIME)
	public abstract Appointment toEntity(AppointmentDto appointmentDto);
	
}
