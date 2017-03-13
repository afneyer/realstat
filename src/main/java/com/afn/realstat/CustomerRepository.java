package com.afn.realstat;

import java.util.List;

public interface CustomerRepository extends AbstractEntityRepository<Customer> {

	List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
	long count();
}
