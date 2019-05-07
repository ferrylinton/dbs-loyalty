package com.dbs.loyalty.domain;


import java.io.Serializable;

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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Product
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "p_product",
	uniqueConstraints = {
		@UniqueConstraint(name = "p_product_name_uq", columnNames = { "name" })
	}
)
public class Product extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "point")
	private Integer point;
    
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @Lob
    @Column(name = "term_and_condition", columnDefinition="TEXT")
    private String termAndCondition;

    @JsonIgnoreProperties("products")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false, foreignKey = @ForeignKey(name = "p_product_fk"))
    private ProductCategory productCategory;
    
}
