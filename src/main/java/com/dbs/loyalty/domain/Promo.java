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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Promo
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "c_promo",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_promo_code_uq", columnNames = { "code" }),
		@UniqueConstraint(name = "c_promo_title_uq", columnNames = { "title" })
	}
)
public class Promo extends AbstractAuditing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
    
    @NotNull(message = "{validation.notnull.code}")
    @Size(min=2, max = 50, message = "{validation.size.code}")
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
    @Column(name = "title", length = 150, nullable = false)
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
    
    @NotNull(message = "{validation.notnull.termAndCondition}")
    @Size(min=2, max = 50000, message = "{validation.size.termAndCondition}")
    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_period", nullable = false)
    private Date startPeriod;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_period", nullable = false)
    private Date endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private boolean showInCarousel;
    
    @Column(name = "activated", nullable = false)
	private boolean activated;

    @JsonIgnoreProperties("promos")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "c_promo_fk"))
    private PromoCategory promoCategory;

    @Transient
    private String image;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileImage;
    
}
