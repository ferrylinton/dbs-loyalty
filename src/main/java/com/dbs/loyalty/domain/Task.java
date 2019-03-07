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

import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;

/**
 * Class of Task
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(name = "t_task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_operation", length = 6, nullable = false)
    private TaskOperation taskOperation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", length = 7, nullable = false)
    private TaskStatus taskStatus;

    @Column(name = "task_data_type", length = 30, nullable = false)
    private String taskDataType;
    
    @Column(name = "task_data_id", length=36)
    private String taskDataId;
    
    @Lob
    @Column(name = "task_data", nullable = false)
    private String taskData;

    @Column(name = "maker", length = 50, nullable = false)
    private String maker;

    @Column(name = "made_date", nullable = false)
    private Instant madeDate;
    
    @ColumnDefault("NULL")
    @Column(name = "checker", length = 50)
    private String checker;

    @ColumnDefault("NULL")
    @Column(name = "checked_date")
    private Instant checkedDate;

    @ColumnDefault("NULL")
    @Column(name = "message")
    private String message;
    
    @ColumnDefault("NULL")
    @Column(name = "error")
    private String error;
    
    @Lob
    @ColumnDefault("NULL")
    @Column(name = "errorDetail")
    private String errorDetail;
    
    @Transient
    private Boolean verified = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaskOperation getTaskOperation() {
		return taskOperation;
	}

	public void setTaskOperation(TaskOperation taskOperation) {
		this.taskOperation = taskOperation;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskDataType() {
		return taskDataType;
	}

	public void setTaskDataType(String taskDataType) {
		this.taskDataType = taskDataType;
	}

	public String getTaskDataId() {
		return taskDataId;
	}

	public void setTaskDataId(String taskDataId) {
		this.taskDataId = taskDataId;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public Instant getMadeDate() {
		return madeDate;
	}

	public void setMadeDate(Instant madeDate) {
		this.madeDate = madeDate;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public Instant getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(Instant checkedDate) {
		this.checkedDate = checkedDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

}
