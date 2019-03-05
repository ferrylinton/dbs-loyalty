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

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public RewardOperation getRewardOperation() {
		return rewardOperation;
	}

	public void setRewardOperation(RewardOperation rewardOperation) {
		this.rewardOperation = rewardOperation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
