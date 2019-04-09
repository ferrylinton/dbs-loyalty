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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

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
public class Promo extends AbstractImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=8)
	@GenericGenerator(name = "StringIdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "StringIdGenerator")
	private String id;
    
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;
    
    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    @Lob
    @Column(name = "image_bytes", nullable = false, columnDefinition="BLOB")
    private byte[] imageBytes;
    
    @Column(name = "image_content_type", length = 50, nullable = false)
    private String imageContentType;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "c_promo_fk"))
    private PromoCategory promoCategory;

}
