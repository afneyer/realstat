package com.afn.realstat;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractEntityRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {
 
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public default void saveOrUpdate(T newEntity) {
		
		List<T> existingEntities = getExistingEntities(newEntity);
		if (existingEntities.size() > 1) {
			log.error("Non-unique entries for table " + newEntity.getClass().getName() + "field apn");
		}

		if (existingEntities.size() >= 1) {
			T existingEntity = existingEntities.get(0);
			if (existingEntity != null) {
				newEntity.setId(existingEntity.getId());
			}
		}
		this.save(newEntity);

	}

	@SuppressWarnings("rawtypes")
	public default List<T> getExistingEntities(T newEntity) {
		Example example = ((AbstractEntity) newEntity).getRefExample();
		@SuppressWarnings("unchecked")
		List<T> list = this.findAll(example);
		return list;
	}
	
}