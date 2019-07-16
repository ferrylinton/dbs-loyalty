package com.dbs.loyalty.domain.prm;

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

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.AbstractTask;
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
	name = "prm_promo",
	uniqueConstraints = {
		@UniqueConstraint(name = "prm_promo_code_uq", columnNames = { "code" }),
		@UniqueConstraint(name = "prm_promo_title_uq", columnNames = { "title" })
	}
)
public class Promo extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = DomainConstant.ID, length=22)
    @GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
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

    @DateTimeFormat(pattern = DateConstant.JAVA_DATE)
    @Column(name = "start_period", nullable = false)
    private LocalDate startPeriod;

    @DateTimeFormat(pattern = DateConstant.JAVA_DATE)
    @Column(name = "end_period", nullable = false)
    private LocalDate endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private boolean showInCarousel;
    
    @Column(name = "activated", nullable = false)
	private boolean activated;

    @JsonIgnoreProperties("promos")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "prm_promo_fk"))
    private PromoCategory promoCategory;

    @Transient
    private String image;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileImage;
    
}
