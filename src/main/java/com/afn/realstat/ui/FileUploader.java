package com.afn.realstat.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.atmosphere.interceptor.AtmosphereResourceStateRecovery.B;

import com.afn.realstat.AddressRepository;
import com.afn.realstat.Artifact;
import com.afn.realstat.ArtifactRepository;
import com.afn.realstat.TourListRepository;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

// Implement both receiver that saves upload in a file and
// listener for successful upload
@SuppressWarnings("serial")
public class FileUploader implements Receiver, SucceededListener {
	
	private File file;
	private FileOutputStream fos;
	private AfterUploadSucceeded afterUploadSucceeded;
	
	private static String filePath = System.getProperty("user.dir") + "\\logs\\uploads\\";

	public FileUploader() {
	}
	
	public void setAfterUploadSucceeded( AfterUploadSucceeded function ) {
		afterUploadSucceeded = function;
	}
	public OutputStream receiveUpload(String filename, String mimeType) {
		fos = null; // Stream to write to
		try {
			// Open the file for writing.
			file = new File(filePath + filename);
			fos = new FileOutputStream(file);		
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return null;
		}
		return this.fos; // Return the output stream to write to
	}

	public void uploadSucceeded(SucceededEvent event) {
		
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			new Notification("Could not close file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
			.show(Page.getCurrent());
		}
		afterUploadSucceeded.afterUploadSucceeded(this);
	}
	
	public File getFile() {
		return file;
	}
};