package com.dbs.loyalty.domain.evt;


import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.AbstractTask;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	name = "evt_event",
	uniqueConstraints = {
		@UniqueConstraint(name = "evt_event_title_uq", columnNames = { "title" })
	}
)
public class Event extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

    @Size(min=2, max = 150)
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Size(min=2, max = 255)
    @Column(name = "description", nullable = false)
    private String description;

    @Size(min=2, max = 50000)
    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;

    @Column(name = "start_period", nullable = false)
    private Instant startPeriod;

    @Column(name = "end_period", nullable = false)
    private Instant endPeriod;
    
    @Size(min=2, max = 255)
    @Column(name = "place", nullable = false)
    private String place;
    
    @JsonIgnoreProperties({"questions", "events"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false, foreignKey = @ForeignKey(name = "evt_event_fk"))
    private Feedback feedback;
    
    @Transient
    private String image;
    
    @Transient
    private String timePeriod;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileImage;
    
    @Transient
    private String material;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileMaterial;
    
}
