package com.afn.realstat.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

// Implement both receiver that saves upload in a file and
// listener for successful upload
@SuppressWarnings("serial")
class FileUpLoader implements Receiver, SucceededListener {

	public ByteArrayOutputStream outStream;
	public ByteArrayInputStream inStream;
	
	private File file;
	private static String filePath = System.getProperty("user.dir") + "\\logs\\analytics";

	/*
	 * public OutputStream receiveUpload(String filename, String mimeType) {
	 * outStream = new ByteArrayOutputStream(); try { outStream.flush(); } catch
	 * (IOException e) { throw new RuntimeException(e); } return outStream; }
	 */

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to
		try {
			// Open the file for writing.
			file = new File(filePath + filename);
			fos = new FileOutputStream(file);		
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return null;
		}
		return fos; // Return the output stream to write to
	}

	public void uploadSucceeded(SucceededEvent event) {
		// put the file outStream into a ByteArray
		byte[] byteArray = outStream.toByteArray();
		inStream = new ByteArrayInputStream(byteArray);

	}

	public InputStream getInStream() {
		return inStream;
	}

	public File getFile() {
		return file;
	}
};

/*
 * ImageUploader receiver = new ImageUploader();
 * 
 * // Create the upload with a caption and set receiver later Upload upload =
 * new Upload("Upload Image Here", receiver);
 * upload.addSucceededListener(receiver);
 */