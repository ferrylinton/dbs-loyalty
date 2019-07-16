package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of Log API Item
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(
	name = "log_api_url",
	uniqueConstraints = {
		@UniqueConstraint(name = "log_api_url_uq", columnNames = { "url" })
})
public class LogApiUrl implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @Column(name = "url", length=100, nullable = false)
    private String url;

    @Column(name = "logged", nullable = false)
	private Boolean logged = false;
    
}
