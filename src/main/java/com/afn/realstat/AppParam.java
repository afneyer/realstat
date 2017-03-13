package com.afn.realstat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

@Entity
@Table(name = "app_param", indexes = { @Index(name = "idx_key_type", columnList = "key,paramType") })
public class AppParam extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");

	private String key;
	private String paramType;
	private String value;
	private String description;

	public AppParam() {
	}

	public AppParam(String key, String paramType, String value, String description) {
		this.key = key;
		this.paramType = paramType;
		this.value = value;
		this.description = description;
	}

	public String toString() {
		String param = key;
		param += " " + paramType;
		param += " " + value;
		return param;

	}

	@Override
	public void clean() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public Example<AppParam> getRefExample() {
		AppParam param = new AppParam();
		param.key = key;
		param.paramType = paramType;
		Example<AppParam> e = Example.of(param);
		return e;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
