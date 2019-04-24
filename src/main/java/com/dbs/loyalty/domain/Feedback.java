package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "q_feedback")
public class Feedback extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=36)
	private String id;

    @OneToMany(mappedBy = "feedback")
    @MapKeyColumn(name="id")
    private Map<String, FeedbackQuestion> questionMap;
    
}
