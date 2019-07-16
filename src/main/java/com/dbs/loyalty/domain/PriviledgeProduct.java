package com.dbs.loyalty.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Product Priviledge
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(
	name = "trx_priviledge_product",
	uniqueConstraints = {
		@UniqueConstraint(name = "trx_priviledge_product_name_uq", columnNames = { "name" })
	}
)
public class PriviledgeProduct extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "id", length=22)
    @GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
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
    
}
