package com.dbs.loyalty.domain.evt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.dbs.loyalty.util.SecurityUtil;

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
public class FeedbackCustomerId implements Serializable {

	private static final long serialVersionUID = 1L;

	public FeedbackCustomerId(String eventId) {
		this.eventId = eventId;
		this.customerId = SecurityUtil.getId();
	}
	
	@Column(name = "event_id", length=22)
    private String eventId;

	@Column(name = "customer_id", length=22)
    private String customerId;

}
