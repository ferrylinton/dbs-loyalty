package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "c_reward")
public class Reward extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
	
	@Column(name = "point", nullable = false)
	private Integer point;
	
	@Column(name = "expiry_date", nullable = false, columnDefinition = "DATE")
	private LocalDate expiryDate;

	@ManyToOne
    @JoinColumn(name = "reward_operation_id", nullable = false, foreignKey = @ForeignKey(name = "c_reward_fk1"))
	private RewardOperation rewardOperation;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "c_reward_fk2"))
    private Customer customer;

}
