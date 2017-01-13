package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface RealPropertyRepository extends AbstractEntityRepository<RealProperty> {

	List<RealProperty> findByLastNameStartsWithIgnoreCase(String lastName);

	List<RealProperty> findByApn(String apn);
	
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	

	/* TOOD remove
	public default void saveOrUpdate(RealProperty newEntity) {

		List<RealProperty> existingEntities = findByApn(newEntity.getApn());
		if (existingEntities.size() > 1) {
			log.error("Non-unique entries for table " + newEntity.getClass().getName() + "field apn");
		}

		if (existingEntities.size() >= 1) {
			RealProperty existingEntity = existingEntities.get(0);
			if (existingEntity != null) {
				newEntity.setId(existingEntity.getId());
			}
		}
		this.save(newEntity);

	}
	
	*/
}
