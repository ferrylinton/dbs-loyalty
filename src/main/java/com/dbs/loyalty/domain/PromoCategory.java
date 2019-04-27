package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.Constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Promo Category
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="PromoCategory", description = "Promo Category's data")
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "p_promo_category",
	uniqueConstraints = {
			@UniqueConstraint(name = "c_promo_category_name_uq", columnNames = { "name" })
	}
)
public class PromoCategory extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @ApiModelProperty(value = "Promo Category's id", example = "98781658-6977-42b7-8da5-31b84560bc71", required = true, position = 0)
    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
    
    @ApiModelProperty(value = "Promo Category's name", example = "Debit Card Promo", required = true, position = 0)
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
    @Size(min = 2, max = 100, message = "{validation.size.name}")
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "promoCategory")
    private Set<Promo> promos = new HashSet<>();

    @Override
	public String toString() {
    	return id + "," + name;
	}
    
}
