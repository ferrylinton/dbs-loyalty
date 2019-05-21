package com.dbs.loyalty.domain;

import static com.dbs.loyalty.config.constant.DomainConstant.ID;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR_STRATEGY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of MedicalProviderBranch
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(	
	name = "a_medical_provider_branch", 
	uniqueConstraints = {
		@UniqueConstraint(name = "a_medical_provider_branch_address_uq", columnNames = {"address"})
	}
)
public class MedicalProviderBranch extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = ID, length=22)
    @GenericGenerator(name = ID_GENERATOR, strategy = ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = ID_GENERATOR)
	private String id;

	@Size(min = 2, max = 150, message = "{validation.size.address}")
    @Column(name = "address", length = 150, nullable = false)
    private String address;

	@JsonIgnoreProperties("medicalProviderBranches")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_provider_city_id", nullable = false, foreignKey = @ForeignKey(name = "a_medical_provider_branch_fk"))
    private MedicalProviderCity medicalProviderCity;
	
}
