package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Promo Category
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "name" }, callSuper = true)
@ToString
@Entity
@Table(
	name = "c_promo_category",
	uniqueConstraints = {
			@UniqueConstraint(name = "c_promo_category_name_uq", columnNames = { "name" })
	}
)
public class PromoCategory extends AbstractId implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "promoCategory")
    private Set<Promo> promos = new HashSet<>();

}
