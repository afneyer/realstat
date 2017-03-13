package com.afn.realstat;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class AddressManager extends AbstractEntityManager<Address> {

	public static final Logger log = LoggerFactory.getLogger("import");
	
	@Autowired
	AddressRepository adrRepo;

	public AddressManager(AddressRepository adrRepo) {
		repo = adrRepo;
	}

public void cleanAndFixAddresses() {
		
		Function<Address, Boolean> cleanAndFixAddress = adr -> {
			return this.cleanAndFixAddressUsingMapLocations(adr);
		};
		
		QAddress qAdr = QAddress.address;
		BooleanExpression predicate = qAdr.location.isNull();
		performActionOnEntities(cleanAndFixAddress, predicate);
	}
	
	public Boolean cleanAndFixAddressUsingMapLocations(Address adr) {
		if (adr != null) {
			adr.setMapLocationFields();
			System.out.println("Fixing address using MapLocations: " + adr);
			return true;
		}
		return false;
	}
	
	
}
