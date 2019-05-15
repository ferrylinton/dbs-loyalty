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
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Product Category
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "p_product_category",
	uniqueConstraints = {
			@UniqueConstraint(name = "p_product_category_name_uq", columnNames = { "name" })
	}
)
public class ProductCategory extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
	@Column(name = "id", length=22)
    @GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
    
    @Size(min = 2, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "productCategory")
    private Set<Product> products = new HashSet<>();

    @Override
	public String toString() {
    	return id + "," + name;
	}
    
}
