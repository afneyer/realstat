package com.afn.realstat;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class AddressManager extends AbstractEntityManager<Address> {

	@Autowired
	AddressRepository adrRepo;

	@Autowired
	RealPropertyRepository rpRepo;

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
			Boolean b = adr.setMapLocationFields();
			if (b) {
				System.out.println("Fixing address using MapLocations: " + adr);
			} else {
				log.warn("Could not set location for address=" + adr);
			}
			return b;
		}
		return false;
	}

	public Point getLocation(Address adr) {
		Point p = null;
		if (adr != null) {
			p = adr.getLocation();
			if (p == null) {
				if (adr.setMapLocationFields()) {
					p = adr.getLocation();
					if (p != null) {
						adrRepo.save(adr);
					}
				}
			}
		}
		return p;
	}

	public void removeAllDuplicates() {

		/*
		 * Function<Address, Boolean> removeDups = adr -> { return
		 * this.removeDuplicates(adr); };
		 * 
		 */
		// QAddress qAdr = QAddress.address;
		// BooleanExpression predicate =
		// qAdr.zip.eq("94610").and(qAdr.city.eq("PIEDMONT"))
		// .and(qAdr.streetName.startsWith("INDIAN"));

		Iterable<Address> adrList = adrRepo.findAll();
		for (Address adr : adrList) {
			removeDuplicates(adr);
		}

		// performActionOnEntities(removeDups, predicate);
	}

	@Transactional
	public Boolean removeDuplicates(Address adr) {
		log.warn("Removing duplicates from adresses: " + adr.getDetails());
		Boolean ret = true;
		Example<Address> example = adr.getRefExample();
		long numFound = adrRepo.count(example);
		log.info("Found " + numFound + " addresses");

		// if multiple addresses are found try to delete this one
		if (numFound > 1) {
			log.warn("Found " + numFound + " duplicates of address " + adr.getDetails());

			BooleanExpression predicate = QRealProperty.realProperty.propertyAdr.eq(adr);
			// check whether other entities point to this transaction
			long count = rpRepo.count(predicate);
			if (count == 0) {
				try {
					adrRepo.delete(adr);
					adrRepo.flush();
					log.info("Deleted address:" + adr);
				} catch (Exception e) {
					log.warn("Cannnot delete address: " + adr);
					log.warn(e.toString());
					throw new RuntimeException(e);
				}
			} else {
				log.warn("Cannot delete address because a real property uses it! Adress: " + adr.getDetails());
			}
		}
		if (numFound == 0) {
			log.error("Could not find Address with itself as example: " + adr);
		}
		return ret;
	}
}
