package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class UUIDGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
		for(int i=0; i<100; i++) {
			System.out.println( UUID.randomUUID().toString());
		}
	}
	
}