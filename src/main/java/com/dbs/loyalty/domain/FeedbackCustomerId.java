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
public class FeedbackCustomerId implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	@Column(name = "feedback_id", length=36)
    private String feedbackId;
	
	@NonNull
	@Column(name = "customer_id", length=36)
    private String customerId;

}
