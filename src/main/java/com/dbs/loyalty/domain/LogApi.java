package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
			@Index(name = "log_api_created_by_idx", columnList = "created_by"),
			@Index(name = "log_api_created_date_idx", columnList = "created_date"),
			@Index(name = "log_api_request_uri_idx", columnList = "request_uri")
	}
)
public class LogApi implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

    @Column(name = "request_uri")
    private String requestURI;
    
    @Column(name = "query_string")
    private String queryString;
    
    @Column(name = "method")
    private String method;
    
    @Column(name = "status")
    private Integer status;

    @Lob
    @Column(name = "body")
    private String body;

    @Lob
    @Column(name = "error")
    private String error;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

	@Override
	public String toString() {
		return "[" + requestURI + "," + queryString + "," + method + "," + status + "," + createdBy + "]";
	}
    
    
}
