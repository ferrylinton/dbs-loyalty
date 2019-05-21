package com.dbs.loyalty.domain;

import java.time.LocalDate;

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

	@Column(name = "message")
	private String message;
	
	@Column(name = "arrival_date", nullable = false)
	private LocalDate arrivalDate;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "health_partner_id", nullable = false, foreignKey = @ForeignKey(name = "a_appointment_fk1"))
    private MedicalProvider healthPartner;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "a_appointment_fk2"))
	private Customer customer;
	
}
