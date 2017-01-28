package com.afn.realstat;

import java.util.List;

public interface AgentRepository extends AbstractEntityRepository<Agent> {

	List<Agent> findByLicense(String license);
	
	// public static final Logger log = LoggerFactory.getLogger(Application.class);
	
}