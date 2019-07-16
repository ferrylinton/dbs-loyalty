package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.MedicalProviderBranch;
import com.dbs.loyalty.service.dto.MedicalProviderBranchDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MedicalProviderBranchMapper{

	public abstract MedicalProviderBranch toEntity(MedicalProviderBranchDto medicalProviderBranch);
	
	public abstract MedicalProviderBranchDto toDto(MedicalProviderBranch medicalProviderBranch);
	
	public abstract List<MedicalProviderBranchDto> toDto(List<MedicalProviderBranch> medicalProviderCities);
	
}
