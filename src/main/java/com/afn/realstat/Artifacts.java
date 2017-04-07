package com.afn.realstat;

import java.io.File;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

@Entity
// TODO fix
@Table(name = "agent",  indexes = {
		@Index(name = "idx_firstName", columnList = "firstName"),
		@Index(name = "idx_lastName", columnList = "lastName"), @Index(name = "idx_license", columnList = "license") })

/*
 * TODO this class needs to be finished
 */
public class Artifacts extends AbstractEntity {

	public static final Logger log = LoggerFactory.getLogger("app");
	public static final Class<Artifacts> classType = Artifacts.class;

	private String identifier;
	private String category;
	private String description;
	private String fileName;
	private Blob content;
	
	private String mimeType;
	private String mimeSubtype;
	private Date created;
	private Date deleteAfter;

	public Artifacts() {
	}

	public Artifacts(File file, String description) {
	}

	public Artifacts(String description, Blob blob, String contentType) {
	}

	@Override
	public void clean() {
	}
	
	@Override
	public String toString() {
		String str = description + " " + fileName;
		return str;
	}

	@Override
	public Example<Artifacts> getRefExample() {
		Artifacts sample = new Artifacts();
		sample.identifier = identifier;
		sample.category = category;
		Example<Artifacts> e = Example.of(sample);

		return e;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeSubtype() {
		return mimeSubtype;
	}

	public void setMimeSubtype(String mimeSubtype) {
		this.mimeSubtype = mimeSubtype;
	}


	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleteAfter() {
		return deleteAfter;
	}

	public void setDeleteAfter(Date deleteAfter) {
		this.deleteAfter = deleteAfter;
	}
	
	
}
