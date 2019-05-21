package com.dbs.loyalty.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Aiport Assistance
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
	private String id;

	@Column(name = "total", nullable = false)
	private int total;
	
}
