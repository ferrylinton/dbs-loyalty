package com.dbs.loyalty.domain.evt;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.dbs.loyalty.domain.cst.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "evt_feedback_customer")
public class FeedbackCustomer implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FeedbackCustomerId id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "evt_feedback_customer_fk1"))
	private Customer customer;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "evt_feedback_customer_fk2"))
	private Event event;

	@JsonIgnoreProperties("feedbackCustomer")
	@OneToMany(mappedBy = "feedbackCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy(value = "questionNumber ASC")
    private Set<FeedbackAnswer> answers;
	
    @JsonIgnore
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
	
}
