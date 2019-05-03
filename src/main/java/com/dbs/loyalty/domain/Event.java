package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	name = "e_event",
	uniqueConstraints = {
		@UniqueConstraint(name = "e_event_title_uq", columnNames = { "title" })
	}
)
public class Event extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

    @NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "{validation.notnull.content}")
    @Size(min=2, max = 50000, message = "{validation.size.content}")
    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @NotNull(message = "{validation.notnull.startPeriod}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_period", nullable = false)
    private Date startPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @NotNull(message = "{validation.notnull.endPeriod}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_period", nullable = false)
    private Date endPeriod;
    
    @NotNull(message = "{validation.notnull.place}")
    @Size(min=2, max = 255, message = "{validation.size.place}")
    @Column(name = "place", nullable = false)
    private String place;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false, foreignKey = @ForeignKey(name = "e_event_fk"))
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
