package com.afn.realstat;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class AddressManager extends AbstractEntityManager<Address> {

	public static final Logger log = LoggerFactory.getLogger("log");
	
	@Autowired
	AddressRepository adrRepo;

	// move the superclass
	public AddressManager(AddressRepository adrRepo) {
		repo = adrRepo;
	}

public void cleanAndFixAddresses() {
		
		Function<Address, Boolean> cleanAndFixAddress = adr -> {
			return this.cleanAndFixAddressUsingMapLocations(adr);
		};
		
		QAddress qAdr = QAddress.address;
		BooleanExpression predicate = qAdr.location.isNull().and(qAdr.mapLocCalls.eq(0));
		performActionOnEntities(cleanAndFixAddress, predicate);
	}
	
	public Boolean cleanAndFixAddressUsingMapLocations(Address adr) {
		if (adr != null) {
			Boolean b  = adr.setMapLocationFields();
			if (b) {
				System.out.println("Fixing address using MapLocations: " + adr);
			} else {
				log.warn("Could not set location for address=" + adr);
			}
			return b;
		}
		return false;
	}
	
	
}
