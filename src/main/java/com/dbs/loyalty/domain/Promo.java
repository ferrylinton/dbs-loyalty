package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class of Promo
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
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

    @NotNull(message = "{validation.notnull.code}")
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @NotNull(message = "{validation.notnull.title}")
    @Size(max = 150)
    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @NotNull(message = "{validation.notnull.description}")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "{validation.notnull.termAndCondition}")
    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    
    @Lob
    @Column(name = "image_bytes", nullable = false, columnDefinition="MEDIUMBLOB")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageBytes;
    
    @NotNull(message = "{validation.notnull.file}")
    @JsonIgnore
    @Transient
    private MultipartFile file;

    @NotNull(message = "{validation.notnull.startPeriod}")
    @Column(name = "start_period", nullable = false)
    private Instant startPeriod;

    @NotNull(message = "{validation.notnull.endPeriod}")
    @Column(name = "end_period", nullable = false)
    private Instant endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private Boolean showInCarousel = false;
    
    @Column(name = "activated", nullable = false)
	private Boolean activated = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_category_id", nullable = false, foreignKey = @ForeignKey(name = "c_promo_fk"))
    private PromoCategory promoCategory;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTermAndCondition() {
		return termAndCondition;
	}

	public void setTermAndCondition(String termAndCondition) {
		this.termAndCondition = termAndCondition;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Instant getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(Instant startPeriod) {
		this.startPeriod = startPeriod;
	}

	public Instant getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(Instant endPeriod) {
		this.endPeriod = endPeriod;
	}

	public Boolean getShowInCarousel() {
		return showInCarousel;
	}

	public void setShowInCarousel(Boolean showInCarousel) {
		this.showInCarousel = showInCarousel;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public PromoCategory getPromoCategory() {
		return promoCategory;
	}

	public void setPromoCategory(PromoCategory promoCategory) {
		this.promoCategory = promoCategory;
	}

}
