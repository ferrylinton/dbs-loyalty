package com.dbs.loyalty.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dbs.loyalty.domain.enumeration.OperationType;

@Entity
@Table(	
	name = "c_reward_operation",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_reward_operation_description_uq", columnNames = { "description" })
	}
)
public class RewardOperation extends AbstractAuditing{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=3)
	private String id;
	
	@NotNull(message = "{validation.notnull.description}")
    @Size(min = 2, max = 100, message = "{validation.size.description}")
    @Column(name = "description", length = 100, nullable = false)
    private String description;
	
	@Column(name = "operation_type", length = 1, nullable = false, columnDefinition = "TINYINT")
	@Enumerated(EnumType.ORDINAL)
	private OperationType operationType;
	
	@Column(name = "point", nullable = false)
	private Integer point;
	
	@Column(name = "validity_period", length = 3, nullable = false, columnDefinition = "TINYINT")
	private Integer validityPeriod;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(Integer validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
}
