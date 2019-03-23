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

import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;

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
    @Column(name = "task_data_old")
    private String taskDataOld;
    
    @Lob
    @Column(name = "task_data_new")
    private String taskDataNew;

    @Column(name = "maker", length = 50, nullable = false)
    private String maker;

    @Column(name = "made_date", nullable = false)
    private Instant madeDate;
    
    @Column(name = "checker", length = 50)
    private String checker;

    @Column(name = "checked_date")
    private Instant checkedDate;

    @Column(name = "message")
    private String message;
    
    @Column(name = "error")
    private String error;
    
    @Lob
    @Column(name = "errorDetail")
    private String errorDetail;

}
