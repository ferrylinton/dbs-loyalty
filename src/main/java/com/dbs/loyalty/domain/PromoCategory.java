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
import javax.validation.constraints.Size;

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
  
    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

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
