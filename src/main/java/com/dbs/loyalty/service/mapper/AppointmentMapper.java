package com.dbs.loyalty.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.domain.med.Appointment;
import com.dbs.loyalty.service.dto.AppointmentDto;

@Mapper(
		componentModel="spring", 
		uses={MedicalProviderMapper.class, MedicalProviderCityMapper.class, MedicalProviderBranchMapper.class, HealthPackageMapper.class}, 
		injectionStrategy=InjectionStrategy.CONSTRUCTOR)
public abstract class AppointmentMapper{

	@Mapping(source="date", target="date", dateFormat=DateConstant.JAVA_DATE)
	@Mapping(source="time", target="time", dateFormat=DateConstant.JAVA_TIME)
	public abstract Appointment toEntity(AppointmentDto appointmentDto);
	
}
