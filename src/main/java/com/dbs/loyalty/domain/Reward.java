package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = { "point", "expiryDate" }, callSuper = true)
@Entity
@Table(name = "c_reward")
public class Reward extends AbstractUUID implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
