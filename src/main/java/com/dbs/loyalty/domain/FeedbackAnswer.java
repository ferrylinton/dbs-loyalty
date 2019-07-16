package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id", "questionNumber", "questionText", "questionAnswer"})
@EqualsAndHashCode(of = {"id", "questionNumber", "questionText", "questionAnswer"})
@Entity
@Table(name = "evt_feedback_answer")
public class FeedbackAnswer implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
    
    @Column(name = "question_number", nullable = false, columnDefinition="TINYINT")
    private Integer questionNumber;
    
    @Column(name = "question_text", nullable = false)
    private String questionText;
    
    @Column(name = "question_answer")
    private String questionAnswer;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(
            name = "event_id",
            referencedColumnName = "event_id",
            nullable = false, foreignKey = @ForeignKey(name = "evt_feedback_answer_fk1")),
        @JoinColumn(
            name = "customer_id",
            referencedColumnName = "customer_id",
            nullable = false, foreignKey = @ForeignKey(name = "evt_feedback_answer_fk2")),
    })
    private FeedbackCustomer feedbackCustomer;

}
