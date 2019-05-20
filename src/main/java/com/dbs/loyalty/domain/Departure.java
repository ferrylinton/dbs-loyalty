package com.dbs.loyalty.domain;

import static com.dbs.loyalty.config.constant.DomainConstant.ID;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR_STRATEGY;

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
 * Class of Departure
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(name = "a_departure")
public class Departure extends AbstractAuditing {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = ID, length=22)
	@GenericGenerator(name = ID_GENERATOR, strategy = ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = ID_GENERATOR)
	private String id;
	
	@Column(name = "service_type", length=150)
	private String serviceType;

	@Column(name = "flight_date", nullable = false)
	private Instant flightDate;

	@Column(name = "flight_code", nullable = false, length=50)
	private String flightCode;
	
	@Column(name = "flight_number", nullable = false, length=50)
	private String flightNumber;
	
	@Column(name = "customer_phone", length=20)
	private String customerPhone;
	
	@Column(name = "guest_phone", length=20)
	private String guestPhone;
	
	@Column(name = "airport_transfer", nullable = false)
	private boolean airportTransfer;
	
	@Column(name = "number_of_passengers", nullable = false)
	private int numberOfPassengers;
	
	@Column(name = "number_of_luggages", nullable = false)
	private int numberOfLuggages;
	
	@Column(name = "pickup_address")
	private String pickupAddress;
	
	@Column(name = "pickup_time")
	private Instant pickupTime;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_id", nullable = false, foreignKey = @ForeignKey(name = "a_departure_fk1"))
    private Airport airport;
	
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "a_departure_fk2"))
	private Customer customer;
	
}
