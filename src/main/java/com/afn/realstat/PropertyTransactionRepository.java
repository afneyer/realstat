package com.afn.realstat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyTransactionRepository extends JpaRepository<PropertyTransaction, Long> {

	// List<PropertyTransaction> findById(Long id);
}
