package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AgentVolumeRepostitory extends AbstractEntityRepository<AgentVolume> {

	List<AgentVolume> findByBreNo(String breNo);
	
	public static final Logger log = LoggerFactory.getLogger(Application.class);
	
}