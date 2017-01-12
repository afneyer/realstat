package com.afn.realstat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealPropertyRepository extends JpaRepository<RealProperty, Long> {

	List<RealProperty> findByLastNameStartsWithIgnoreCase(String lastName);
	// List<RealProperty> findByApnStartsWithIgnoreCase(String apn);
}
