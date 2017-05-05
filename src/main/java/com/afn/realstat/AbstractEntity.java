package com.afn.realstat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
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
	
	@Override
	public boolean equals( Object o ) {
		
		// If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
 
        /* If the object is not of the same class they are not equal */
        if ((o.getClass() != this.getClass() )) {
            return false;
        }
         
        // Cast to AbstractEntity so we can compare
        AbstractEntity ae = (AbstractEntity) o;
         
        // Abstract entities are equal if they have the same id 
        return ae.getId().equals(this.getId());
	}

	// TODO fix
	@SuppressWarnings("rawtypes")
	public abstract Example getRefExample();

	public abstract void clean();
	
	public abstract boolean isValid();
	
	public abstract void save();

	public abstract void saveOrUpdate();
	
}
