package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class of Promo Category
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(
	name = "c_promo_category",
	uniqueConstraints = {
			@UniqueConstraint(name = "c_promo_category_name_uq", columnNames = { "name" })
	}
)
public class PromoCategory extends AbstractId implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
    @Size(min = 2, max = 100, message = "{validation.size.name}")
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "promoCategory")
    private Set<Promo> promos = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Promo> getPromos() {
		return promos;
	}

	public void setPromos(Set<Promo> promos) {
		this.promos = promos;
	}

	@Override
	public String toString() {
		return getId() + "," + name;
	}

}
