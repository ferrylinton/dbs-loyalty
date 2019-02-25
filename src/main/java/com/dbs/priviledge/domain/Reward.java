package com.dbs.priviledge.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "c_reward",
		indexes= {
				@Index(name = "c_reward_idx", columnList = "customer_id, expiry_date")
			})
public class Reward extends AbstractUUID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "point", nullable = false)
	private Integer point;
	
	@Column(name = "expiry_date", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant expiryDate;

	@ManyToOne
    @JoinColumn(name = "reward_operation_id", foreignKey = @ForeignKey(name = "c_reward_fk1"))
	private RewardOperation rewardOperation;
	
	@ManyToOne
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "c_reward_fk2"))
    private Customer customer;

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
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
