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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.ApprovalType;
import com.dbs.loyalty.domain.enumeration.DataType;

/**
 * Class of Approval
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(name = "t_approval")
public class Approval implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", length = 30, nullable = false)
    private DataType dataType;

    @Lob
    @Column(name = "data_json", nullable = false)
    private String dataJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_type", length = 8, nullable = false)
    private ApprovalType approvalType;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;
    
    @ColumnDefault("NULL")
    @Column(name = "approved_by", length = 50, nullable = true)
    private String approvedBy;

    @ColumnDefault("NULL")
    @Column(name = "approved_date", nullable = true)
    private Instant approvedDate;

    @ColumnDefault("NULL")
    @Column(name = "message", nullable = true)
    private String message;
    
    @Transient
    private Boolean approved;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public ApprovalType getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(ApprovalType approvalType) {
		this.approvalType = approvalType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Instant getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Instant approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

}
