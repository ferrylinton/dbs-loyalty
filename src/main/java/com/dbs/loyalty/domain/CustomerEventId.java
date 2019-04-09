package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class CustomerEventId implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	@Column(name = "customer_id", length=36)
    private String customerId;
 
	@NonNull
	@Column(name = "event_id", length=8)
    private String eventId;
    
}
