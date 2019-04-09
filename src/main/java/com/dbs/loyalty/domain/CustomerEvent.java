package com.dbs.loyalty.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dbs.loyalty.domain.enumeration.EventAnswer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "c_customer_event")
public class CustomerEvent extends AbstractAuditing {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CustomerEventId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "c_customer_event_fk1"))
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "c_customer_event_fk2"))
	private Event event;
	
	@Enumerated(EnumType.ORDINAL)
    @Column(name = "event_answer", nullable = false, columnDefinition="TINYINT")
    private EventAnswer eventAnswer;
	
}
