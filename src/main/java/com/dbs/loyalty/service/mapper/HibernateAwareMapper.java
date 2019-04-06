package com.dbs.loyalty.service.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.collection.spi.PersistentCollection;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;

@Mapper
public interface HibernateAwareMapper {

	@BeforeMapping
	default <T> Set<T> fixLazyLoadingSet(Collection<?> c, @TargetType Class<?> targetType) {
		if (!Util.wasInitialized(c)) {
			return Collections.emptySet();
		}
		return null;
	}

	@BeforeMapping
	default <T> List<T> fixLazyLoadingList(Collection<?> c, @TargetType Class<?> targetType) {
		if (!Util.wasInitialized(c)) {
			return Collections.emptyList();
		}
		return null;
	}

	class Util {
		static boolean wasInitialized(Object c) {
			if (!(c instanceof PersistentCollection)) {
				return true;
			}

			PersistentCollection pc = (PersistentCollection) c;
			return pc.wasInitialized();
		}
	}
	
}
