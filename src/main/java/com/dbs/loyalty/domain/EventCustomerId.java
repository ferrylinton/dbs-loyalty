package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class EventCustomerId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "event_id", length=22)
    private String eventId;

	@Column(name = "customer_id", length=22)
    private String customerId;
 
}
