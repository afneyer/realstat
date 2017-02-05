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

	public default T saveOrUpdate(T newEntity) {

		newEntity.clean();
		List<T> existingEntities = getExistingEntities(newEntity);
		if (existingEntities.size() > 1) {
			log.error("Non-unique entries for table " + newEntity.getClass().getName() +  newEntity.toString());
			return null;
		}

		if (existingEntities.size() == 1) {
			newEntity = updateEntity(existingEntities.get(0),newEntity);
			return newEntity;
		} else {
			save(newEntity);
			return newEntity;
		}
	}
	
	// assumes the entity exists in the repository
	public default T updateEntity(T existingEntity, T newEntity) {
		// TODO check for detached
		if (existingEntity != null) {
			newEntity.setId(existingEntity.getId());
		    save(newEntity);
		    return newEntity;
		} 
		return null;
	}

	@SuppressWarnings("rawtypes")
	public default List<T> getExistingEntities(T newEntity) {
		Example example = ((AbstractEntity) newEntity).getRefExample();
		@SuppressWarnings("unchecked")
		List<T> list = this.findAll(example);
		return list;
	}

}