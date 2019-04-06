package com.dbs.loyalty.service.mapper;

import org.hibernate.collection.spi.PersistentCollection;

public class HibernatMapperUtil {

	public static boolean wasInitialized(Object c) {
		if (!(c instanceof PersistentCollection)) {
			return true;
		}

		PersistentCollection pc = (PersistentCollection) c;
		return pc.wasInitialized();
	}
	
}
