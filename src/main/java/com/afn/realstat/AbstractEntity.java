package com.afn.realstat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format( "Entity Class = %s, Id= %2 ]", this.getClass(), id );
	}
	
}
