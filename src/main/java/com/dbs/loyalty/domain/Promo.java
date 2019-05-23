package com.dbs.loyalty.domain;


import static com.dbs.loyalty.config.constant.DomainConstant.ID;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR_STRATEGY;
import static com.dbs.loyalty.service.SettingService.JAVA_DATE;

import java.io.Serializable;
import java.time.LocalDate;

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
import org.springframework.format.annotation.DateTimeFormat;
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
	name = "p_promo",
	uniqueConstraints = {
		@UniqueConstraint(name = "p_promo_code_uq", columnNames = { "code" }),
		@UniqueConstraint(name = "p_promo_title_uq", columnNames = { "title" })
	}
)
public class Promo extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = ID, length=22)
    @GenericGenerator(name = ID_GENERATOR, strategy = ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = ID_GENERATOR)
	private String id;
    
    @Size(min=2, max = 50)
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Size(min=2, max = 150)
    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Size(min=2, max = 255)
    @Column(name = "description", nullable = false)
    private String description;
    
    @Size(min=2, max = 50000)
    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;
    
    @Size(min=2, max = 50000)
    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    @DateTimeFormat(pattern = JAVA_DATE)
    @Column(name = "start_period", nullable = false)
    private LocalDate startPeriod;

    @DateTimeFormat(pattern = JAVA_DATE)
    @Column(name = "end_period", nullable = false)
    private LocalDate endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private boolean showInCarousel;
    
    @Column(name = "activated", nullable = false)
	private boolean activated;

    @JsonIgnoreProperties("promos")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "p_promo_fk"))
    private PromoCategory promoCategory;

    @Transient
    private String image;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileImage;
    
}
