package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.AuditStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of Task
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(name = "log_audit")
public class LogAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

    @Column(name = "audit_operation", length=50, nullable = false)
    private String auditOperation;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "audit_status", nullable = false, columnDefinition="TINYINT")
    private AuditStatus auditStatus;
    
    @Lob
    @Column(name = "audit_data")
    private String auditData;

    @Lob
    @Column(name = "audit_error")
    private String auditError;

    @Column(name = "created_by", length = 50, nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
}
