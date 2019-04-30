package com.dbs.loyalty.domain;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.devskiller.friendly_id.FriendlyId;


public class IdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		return FriendlyId.createFriendlyId();
	}

}