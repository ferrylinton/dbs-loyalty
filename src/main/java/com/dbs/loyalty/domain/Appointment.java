package com.dbs.loyalty.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Appointment
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(name = "a_appointment")
public class Appointment extends AbstractAuditing {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

	@Column(name = "date", nullable = false)
	private Instant date;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_provider_id", foreignKey = @ForeignKey(name = "a_appointment_fk1"))
    private MedicalProvider medicalProvider;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_provider_city_id", foreignKey = @ForeignKey(name = "a_appointment_fk2"))
    private MedicalProviderCity medicalProviderCity;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_provider_branch_id", foreignKey = @ForeignKey(name = "a_appointment_fk3"))
    private MedicalProviderBranch medicalProviderBranch;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "a_appointment_fk4"))
	private Customer customer;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "health_package_id", nullable = true, foreignKey = @ForeignKey(name = "a_appointment_fk5"))
    private HealthPackage healthPackage;
	
}
