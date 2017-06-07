package com.afn.realstat.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.afn.realstat.AppFiles;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;

public class Icon {

	public static Integer MarkerButtonBlue = 1;
	public static Integer MarkerButtonGreen = 2;
	public static Integer MarkerButtonRed = 3;

	private File inIconFile = null;

	BufferedImage image = null;

	private final static Map<Integer, String> iconMap;

	static {
		Map<Integer, String> aMap = new HashMap<Integer, String>();
		aMap.put(MarkerButtonBlue, "MarkerButtonBlue.png");
		aMap.put(MarkerButtonGreen, "MarkerButtonGreen.png");
		aMap.put(MarkerButtonRed, "MarkerButtonRed.png");
		iconMap = Collections.unmodifiableMap(aMap);
	}

	public Icon(Integer icon) {
		String iconFileName = iconMap.get(icon);
		loadIcon(iconFileName);
	}

	public Icon(Integer icon, String text) {
		this(icon);
		addText(text);
	}
	
	public Icon(Integer icon, String text, Double scaleFactor) {
		this(icon,text);
		scale(scaleFactor);
	}

	public void addText(String text) {

		Graphics2D grph = image.createGraphics();
		grph.setColor(Color.white);
		Font font = new Font(null, Font.BOLD, 20);
		grph.setFont(font);
		int x = 5;
		int y = 23;
		grph.drawString(text, x, y);
		grph.drawImage(image, 0, 0, null);
		grph.dispose();

	}

	private void loadIcon(String iconFileName) {

		String iconDirName = AppFiles.getIconDir();
		String iconFullFileName = iconDirName + "\\" + iconFileName;
		inIconFile = new File(iconFullFileName);
		image = null;
		try {
			image = ImageIO.read(inIconFile);
		} catch (IOException e) {
			throw new RuntimeException("Error in Icon.loadIcon: invalid File Name" + iconFullFileName, e);
		}
	}

	public void scale(double scaleFactor) {

		int origWidth = image.getWidth();
		int origHeight = image.getHeight();

		int newWidth = (int) Math.round(scaleFactor * origWidth);
		int newHeight = (int) Math.round(scaleFactor * origHeight);

		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D grph = (Graphics2D) newImage.getGraphics();
		grph.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		grph.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		grph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grph.scale(scaleFactor, scaleFactor);

		// everything drawn with grph from now on will get scaled.
		grph.drawImage(image, 0, 0, null);
		image = newImage;
		grph.dispose();

	}

	/*
	 * writes the image to a text file and returns the link URL in string format
	 * as needed by VAADIN's GoogleMapMarker *
	 */
	public String getIconUrl() {

		// TODO String tmpDir = AppFiles.getIconDir();
		String tmpDir = "/VAADIN/";
		String suffix = "." + FilenameUtils.getExtension(inIconFile.getName());
		String prefix = FilenameUtils.getBaseName(inIconFile.getName());

		File file = null;

		try {

			file = File.createTempFile(prefix, suffix, new File(AppFiles.getTempDir()));
			FileOutputStream fos = new FileOutputStream(file);
			ImageIO.write(image, "png", fos);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error in Icon.getIconUrl: Cannot write tempory file" + tmpDir + AppFiles.getTempDir(), e);
		}
		String path = file.getPath();
		
		// TODO temporary solution to be fixed
		path = "/VAADIN/" + prefix + suffix;
		
		return path;

	}
	
	// TODO unfinished, remove
	
	public String getIconUrl01() {
		String url = null;
		Resource resource = null;
		FileDownloader downloader = new FileDownloader(resource);
		// downloader.
		return url;
	}

}