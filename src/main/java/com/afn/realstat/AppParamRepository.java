package com.afn.realstat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AppParamRepository extends AbstractEntityRepository<AppParam> {

	public static final Logger log = LoggerFactory.getLogger("app");

	List<AppParam> findOneByKeyAndParamType(String key, String paramType);

}