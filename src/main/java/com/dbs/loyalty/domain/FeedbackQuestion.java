package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.FormType;
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
@Table(name = "q_feedback_question")
public class FeedbackQuestion extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
    
    @Column(name = "question_number", nullable = false, columnDefinition="TINYINT")
    private Integer questionNumber;
    
    @NotNull(message = "{validation.notnull.questionText}")
    @Size(min=2, max = 255, message = "{validation.size.questionText}")
    @Column(name = "question_text", nullable = false)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "form_type", length=20, nullable = false)
    private FormType formType;

    @Lob
    @Column(name = "question_option", columnDefinition="TEXT")
    private String questionOption;
    
    @JsonIgnoreProperties("questionMap")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false, foreignKey = @ForeignKey(name = "q_feedback_question_fk"))
    private Feedback feedback;
}
