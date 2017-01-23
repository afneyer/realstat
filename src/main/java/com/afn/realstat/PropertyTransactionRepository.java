package com.afn.realstat;

import java.util.List;

public interface PropertyTransactionRepository extends AbstractEntityRepository<PropertyTransaction> {

	// List<PropertyTransaction> findById(Long id);
	List<PropertyTransaction> findByApnClean(String apnClean);
}
