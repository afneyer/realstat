package com.afn.realstat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.domain.Example;

import com.afn.realstat.framework.SpringApplicationContext;

@MappedSuperclass
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long inId ) {
		id = inId;
	}
	
	@Override
	public String toString() {
		return String.format( "Entity Class = %s, Id= %2 ]", this.getClass(), id );
	}

	// TODO fix
	@SuppressWarnings("rawtypes")
	public abstract Example getRefExample();

	public abstract void clean();
	
	public abstract boolean isValid();
	
	public abstract void save();

	public abstract void saveOrUpdate();
	
}
