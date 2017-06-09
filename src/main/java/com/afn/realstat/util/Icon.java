package com.afn.realstat.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

	public static Integer markerButtonBlue = 1;
	public static Integer markerButtonGreen = 2;
	public static Integer markerButtonRed = 3;
	public static Integer emptyIcon = 4;

	private File iconFile = null;
	private Integer iconId = null;
	private String text = null;
	private Double scaleFactor = null;

	BufferedImage image = null;

	private final static Map<Integer, String> iconMap;

	static {
		Map<Integer, String> aMap = new HashMap<Integer, String>();
		aMap.put(markerButtonBlue, "markerButtonBlue.png");
		aMap.put(markerButtonGreen, "markerButtonGreen.png");
		aMap.put(markerButtonRed, "markerButtonRed.png");
		aMap.put(emptyIcon, "emptyIcon.jpg");
		iconMap = Collections.unmodifiableMap(aMap);
	}

	public Icon(Integer iconId) {
		this.iconId = iconId;
		loadIcon();
	}

	public Icon(Integer icon, String text) {
		this(icon);
		this.text = text;
	}

	public Icon(Integer icon, String text, Double scaleFactor) {
		this(icon, text);
		this.scaleFactor = scaleFactor;
	}

	public void addText() {
		drawCenteredText(image, text, Color.WHITE);
	}

	/*
	 * Draws text centered on an image
	 *
	 * Text is scaled to use a font size of 5/8 of the image height
	 * and the text is centered overall on the image.
	 * 
	 * Text color is white for now. 
	 * 
	 */
	private void drawCenteredText(BufferedImage im, String str, Color color) {
		
		int iconHeight = im.getHeight();
		int iconWidth = im.getWidth();

		Graphics2D grph = im.createGraphics();
		grph.setColor(color);

		
		int fontSize = (iconHeight * 5) / 8;
		Font font = new Font(null, Font.BOLD, fontSize);
		grph.setFont(font);
		
		FontMetrics fm = grph.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(str, grph);
		int rlength = (int)bounds.getWidth();
		int rheight = (int)bounds.getHeight();
		
		FontRenderContext frc = 
	            new FontRenderContext(null, true, true);

	    Rectangle2D r2D = font.getStringBounds(text, frc);
		
		int stringWidth = rlength;
		int x = (iconWidth - stringWidth) / 2;
		
		int stringHeight = rheight;
		int y = (iconHeight - stringHeight) / 2 + fontSize;
		grph.drawString(str, x, y);
		
		grph.drawImage(im, 0, 0, null);
		grph.dispose();	
		
	}

	private void loadIcon() {

		String iconFileName = iconMap.get(iconId);
		String iconDirName = AppFiles.getIconDir();
		String iconFullFileName = iconDirName + "\\" + iconFileName;
		iconFile = new File(iconFullFileName);
		image = null;
		try {
			image = ImageIO.read(iconFile);
		} catch (IOException e) {
			throw new RuntimeException("Error in Icon.loadIcon: invalid File Name" + iconFullFileName, e);
		}
	}

	public void scale() {

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
	/*
	 * TODO remove public String getIconUrl() {
	 * 
	 * // TODO String tmpDir = AppFiles.getIconDir(); String tmpDir =
	 * "/VAADIN/"; String suffix = "." +
	 * FilenameUtils.getExtension(iconFile.getName()); String prefix =
	 * FilenameUtils.getBaseName(iconFile.getName());
	 * 
	 * File file = null;
	 * 
	 * try {
	 * 
	 * file = File.createTempFile(prefix, suffix, new
	 * File(AppFiles.getTempDir())); FileOutputStream fos = new
	 * FileOutputStream(file); ImageIO.write(image, "png", fos); } catch
	 * (IOException e) { throw new RuntimeException(
	 * "Error in Icon.getIconUrl: Cannot write tempory file" + tmpDir +
	 * AppFiles.getTempDir(), e); } String path = file.getPath();
	 * 
	 * // TODO temporary solution to be fixed path = "/VAADIN/" + prefix +
	 * suffix;
	 * 
	 * return path;
	 * 
	 * }
	 * 
	 */
	public String getIconUrl() {
		String iconUrl = "/icon/" + iconId;
		if (text != null) {
			iconUrl += "?text=" + text;
		}
		return iconUrl;
	}

	// TODO unfinished, remove

	public byte[] getIcon() {

		if (text != null) {
			addText();
		}

		if (scaleFactor != null) {
			scale();
		}

		byte[] imageInByte = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imageInByte;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(Double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	
	public String getBaseFileName() {
		return iconMap.get(iconId);
	}

}