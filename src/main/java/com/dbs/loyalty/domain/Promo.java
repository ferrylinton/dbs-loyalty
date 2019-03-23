package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

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
@EqualsAndHashCode(of = { "code", "title" }, callSuper = true)
@ToString
@Entity
@Table(
	name = "c_promo",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_promo_code_uq", columnNames = { "code" }),
		@UniqueConstraint(name = "c_promo_title_uq", columnNames = { "title" })
	}
)
public class Promo extends AbstractId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    @Lob
    @Column(name = "image_bytes", nullable = false)
    @Type(type = "org.hibernate.type.BlobType")
    private byte[] imageBytes;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_period", nullable = false)
    private Date startPeriod;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_period", nullable = false)
    private Date endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private Boolean showInCarousel = false;
    
    @Column(name = "activated", nullable = false)
	private Boolean activated = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "c_promo_fk"))
    private PromoCategory promoCategory;

}
