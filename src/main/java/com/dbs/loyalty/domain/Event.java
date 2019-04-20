package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Event
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "c_event",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_event_title_uq", columnNames = { "title" })
	}
)
public class Event extends AbstractImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=8)
	@GenericGenerator(name = "StringIdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "StringIdGenerator")
	private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_period", nullable = false)
    private Date startPeriod;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_period", nullable = false)
    private Date endPeriod;
    
    @Temporal(TemporalType.TIME)
    @Column(name = "time_period", nullable = false)
    private Date timePeriod;
    
    @Column(name = "place", nullable = false)
    private String place;

    @Lob
    @Column(name = "material_bytes", nullable = false, columnDefinition="BLOB")
    private byte[] materialBytes;
    
}
