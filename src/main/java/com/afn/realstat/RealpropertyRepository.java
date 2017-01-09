package com.afn.realstat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealpropertyRepository extends JpaRepository<Realproperty, Long> {

	List<Realproperty> findByLastNameStartsWithIgnoreCase(String lastName);
}
