package com.dbs.loyalty.domain;

import static com.dbs.loyalty.config.constant.DomainConstant.ID;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR_STRATEGY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of MedicalProvider
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(	
	name = "a_medical_provider", 
	uniqueConstraints = {
		@UniqueConstraint(name = "a_medical_provider_name_uq", columnNames = {"name"})
	}
)
public class MedicalProvider extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = ID, length=22)
    @GenericGenerator(name = ID_GENERATOR, strategy = ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = ID_GENERATOR)
	private String id;

	@Size(min = 2, max = 100, message = "{validation.size.name}")
    @Column(name = "name", length = 100, nullable = false)
    private String name;

	@Size(min=2, max = 50000)
    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;
	
	@OneToMany(mappedBy = "medicalProvider")
    private Set<MedicalProviderCity> medicalProviderCities = new HashSet<>();
	
}
