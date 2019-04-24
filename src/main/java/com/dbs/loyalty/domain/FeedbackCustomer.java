package com.dbs.loyalty.domain;

import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "q_feedback_customer")
public class FeedbackCustomer extends AbstractAuditing {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FeedbackCustomerId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "q_feedback_customer_fk1"))
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "q_feedback_customer_fk3"))
	private Feedback feedback;

	@OneToMany(mappedBy = "feedbackCustomer")
    @MapKeyColumn(name="id")
    private Map<String, FeedbackAnswer> answerMap;
	
}
