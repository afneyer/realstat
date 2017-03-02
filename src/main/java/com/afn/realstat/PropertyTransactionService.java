package com.afn.realstat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afn.util.MapLocation;

@Component
public class PropertyTransactionService {

	// private static final Logger LOGGER = Logger.getLogger("app");

	@Autowired
	private PropertyTransactionRepository ptRepo;

	@Autowired
	private AgentRepository agtRepo;

	private PropertyTransactionService() {
	}

	public List<MapLocation> getPropertyLocationsForAgent(String license) {
		List<Agent> agtList = agtRepo.findByLicense(license);
		Agent agent = agtList.get(0);
		List<PropertyTransaction> ptList = ptRepo.findAllTransactionsByAgent(agent);
		List<MapLocation> resultList = new ArrayList<MapLocation>();
		for (PropertyTransaction pt : ptList) {
			String address = pt.getAddress();
			System.out.println(address);
			MapLocation loc = new MapLocation(address);
			resultList.add(loc);
		}
		return resultList;
	}
}
