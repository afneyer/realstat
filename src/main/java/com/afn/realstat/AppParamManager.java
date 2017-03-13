package com.afn.realstat;

import java.util.ArrayList;
import java.util.Date;
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
public class AppParamManager extends AbstractEntityManager<Address> {

	public static final Logger log = LoggerFactory.getLogger("import");
	
	@Autowired
	AppParamRepository apRepo;

	public AppParamManager(AddressRepository apRepo) {
		repo = apRepo;
	}

	public void initializeParameters () {
		
		List<AppParam> list = new ArrayList<AppParam>();
		
		// Google Map Api Parameters (used in MapLocation)
		list.add(new AppParam("maxCallsPerDay", "MAP", "2000", "Maximum number of calls to the Google Maps API per day"));
		list.add(new AppParam("maxCallsPerSecond", "MAP", "50", "Maximum number of calls to the Google Maps API per second"));
		list.add(new AppParam("callsToday", "MAP", "0", "Number of calls made so far today"));
		list.add(new AppParam("lastCall", "MAP", "2017-03-01 10:00:00.000", "Date/time of the last call made"));
		apRepo.save(list);
	}
	
}
