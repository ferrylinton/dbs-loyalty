package com.dbs.loyalty.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.FlightType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Airport Assistance
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(name = "a_airport_assistance")
public class AirportAssistance extends AbstractAuditing {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "flight_type", length=10)
	private FlightType flightType;
	
	@Column(name = "assistance_type", length=150)
	private String assistanceType;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_id", nullable = false, foreignKey = @ForeignKey(name = "a_airport_assistance_fk"))
    private Airport airport;

	@Column(name = "flight_date", nullable = false)
	private LocalDate flightDate;
	
	@Column(name = "flight_time", nullable = false)
	private LocalTime flightTime;

	@Column(name = "flight_code", nullable = false, length=50)
	private String flightCode;
	
	@Column(name = "flight_number", nullable = false, length=50)
	private String flightNumber;
	
	@Column(name = "customer_phone", length=20)
	private String customerPhone;
	
	@Column(name = "guest_phone", length=20)
	private String guestPhone;
	
	@Column(name = "pickup_from_home", nullable = false)
	private boolean pickupFromHome;
	
	@Column(name = "number_of_passengers", nullable = false)
	private int numberOfPassengers;
	
	@Column(name = "number_of_luggages", nullable = false)
	private int numberOfLuggages;
	
	@Column(name = "pickup_address")
	private String pickupAddress;
	
	@Column(name = "pickup_time")
	private LocalTime pickupTime;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "a_airport_assistance_fk"))
	private Customer customer;
	
}
