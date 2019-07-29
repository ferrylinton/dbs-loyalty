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
 * Class of Log Audit Customer
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(
	name = "log_audit_customer",
	indexes= {
			@Index(name = "log_audit_customer_id_idx", columnList = "customer_id"),
			@Index(name = "log_audit_customer_created_date_idx", columnList = "created_date"),
			@Index(name = "log_audit_customer_url_idx", columnList = "url")
	}
)
public class LogAuditCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @Column(name = "operation", length = 100, nullable = false)
    private String operation;
    
    @Column(name = "url", length = 100, nullable = false)
    private String url;
    
    @Column(name = "http_status", nullable = false)
    private Integer httpStatus;

    @Column(name = "status", length = 10, nullable = false, updatable = false)
	private String status;

    @Column(name = "content_type", length = 50)
    private String contentType;
    
    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_data")
    private String requestData;
    
    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "response_data")
    private String responseData;

    @Lob
	@Type(type = "org.hibernate.type.TextType")
    @Column(name = "old_data")
    private String oldData;
    
    @Lob
    @Column(name = "request_file", columnDefinition="MEDIUMBLOB")
    private byte[] requestFile;
    
    @Lob
    @Column(name = "old_file", columnDefinition="MEDIUMBLOB")
    private byte[] oldFile;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "log_audit_customer_fk"))
	private Customer customer;

    @Column(name = "created_date")
    private Instant createdDate;

    
}
