package com.afn.realstat;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

public interface PropertyTransactionRepository extends AbstractEntityRepository<PropertyTransaction> {

	// List<PropertyTransaction> findById(Long id);
	List<PropertyTransaction> findByApnClean(String apnClean);
	
	List<PropertyTransaction> findByCloseDateBetween(Date d1, Date d2);

	List<PropertyTransaction> findByRealPropertyAndCloseDateBefore(RealProperty rp, Date d, Sort sort);

}
