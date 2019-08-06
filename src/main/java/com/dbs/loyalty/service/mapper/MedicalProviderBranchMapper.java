package com.dbs.loyalty.service.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.dbs.loyalty.domain.MedicalBranch;
import com.dbs.loyalty.service.dto.MedicalProviderBranchDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MedicalProviderBranchMapper{

	public abstract MedicalBranch toEntity(MedicalProviderBranchDto medicalProviderBranch);
	
	public abstract MedicalProviderBranchDto toDto(MedicalBranch medicalProviderBranch);
	
	public abstract List<MedicalProviderBranchDto> toDto(List<MedicalBranch> medicalProviderCities);
	
}
