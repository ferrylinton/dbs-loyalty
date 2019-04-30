package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "e_feedback")
public class Feedback extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=22)
	private String id;

    @OneToMany(mappedBy = "feedback", fetch = FetchType.LAZY)
    private Set<FeedbackQuestion> questions;
    
}
