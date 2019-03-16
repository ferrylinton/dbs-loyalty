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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonGetter;
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

    
    @NotNull(message = "{validation.notnull.termAndCondition}")
    @Size(min=2, max = 50000, message = "{validation.size.termAndCondition}")
    @Lob
    @Column(name = "term_and_condition", nullable = false, columnDefinition="TEXT")
    private String termAndCondition;

    @JsonIgnore
    @Lob
    @Column(name = "image_bytes", nullable = false, columnDefinition="MEDIUMBLOB")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imageBytes;
    
    @Transient
    private String imageString;
    
    @JsonIgnore
    @Transient
    private MultipartFile file;

    @NotNull(message = "{validation.notnull.startPeriod}")
    @Temporal(TemporalType.DATE)
    @Column(name = "start_period", nullable = false)
    private Date startPeriod;

    @NotNull(message = "{validation.notnull.endPeriod}")
    @Temporal(TemporalType.DATE)
    @Column(name = "end_period", nullable = false)
    private Date endPeriod;
    
    @Column(name = "show_in_carousel", nullable = false)
	private Boolean showInCarousel = false;
    
    @Column(name = "activated", nullable = false)
	private Boolean activated = true;

    @JsonIgnore
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

	@JsonGetter(value = "imageString")
	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Date getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}

	public Date getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(Date endPeriod) {
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
