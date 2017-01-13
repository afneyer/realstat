package com.afn.realstat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyTransactionRepository extends AbstractEntityRepository<PropertyTransaction> {

	// List<PropertyTransaction> findById(Long id);
}
