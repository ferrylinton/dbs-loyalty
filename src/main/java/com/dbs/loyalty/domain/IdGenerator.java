package com.dbs.loyalty.domain;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class IdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return RandomStringUtils.randomAlphanumeric(8);
	}

	public static void main(String[] args) {
		for(int i=0; i<50; i++) {
			System.out.println(RandomStringUtils.randomAlphanumeric(8));
		}
	}
	
}