package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
public abstract class AbstractId extends AbstractAuditing implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=8)
	@GenericGenerator(name = "StringIdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "StringIdGenerator")
	private String id;

}

