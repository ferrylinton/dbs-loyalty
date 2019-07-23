package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of LogAudit
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(
	name = "log_api",
	indexes= {
			@Index(name = "log_api_customer_id_idx", columnList = "customer_id"),
			@Index(name = "log_api_created_date_idx", columnList = "created_date"),
			@Index(name = "log_api_url_idx", columnList = "url")
	}
)
public class LogApi implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "url", length = 100, nullable = false)
    private String url;

    @Column(name = "status", length = 10, nullable = false, updatable = false)
	private String status;

    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "request")
    private String request;

    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "response")
    private String response;
    
    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "error")
    private String error;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "log_api_fk"))
	private Customer customer;

    @Column(name = "created_date")
    private Instant createdDate;

    
}
