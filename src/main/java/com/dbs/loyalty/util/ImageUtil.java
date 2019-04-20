package com.dbs.loyalty.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.AbstractImage;
import com.dbs.loyalty.service.dto.AbstractImageDto;

public final class ImageUtil {

	public BufferedImage cropImage(byte[] image) throws IOException {
		  // Get a BufferedImage object from a byte array
		  InputStream in = new ByteArrayInputStream(image);
		  BufferedImage originalImage = ImageIO.read(in);
		  
		  // Get image dimensions
		  int height = originalImage.getHeight();
		  int width = originalImage.getWidth();
		  
		  // The image is already a square
		  if (height == width) {
		    return originalImage;
		  }
		  
		  // Compute the size of the square
		  int squareSize = (height > width ? width : height);
		  
		  // Coordinates of the image's middle
		  int xc = width / 2;
		  int yc = height / 2;
		  
		  // Crop
		  BufferedImage croppedImage = originalImage.getSubimage(
		      xc - (squareSize / 2), // x coordinate of the upper-left corner
		      yc - (squareSize / 2), // y coordinate of the upper-left corner
		      squareSize,            // widht
		      squareSize             // height
		  );
		  
		  return croppedImage;
	}
	
	public static <T extends AbstractImageDto> void setImageDto(T t) throws IOException {
		BufferedImage image = getBufferedImage(t.getImageFile());
		t.setImageBytes(t.getImageFile().getBytes());
		t.setImageContentType(t.getImageFile().getContentType());
		t.setImageWidth(image.getHeight());
		t.setImageHeight(image.getHeight());
	}
	
	public static <T extends AbstractImage> void setImageDto(T t, MultipartFile file) throws IOException {
		BufferedImage image = getBufferedImage(file);
		t.setImageBytes(file.getBytes());
		t.setImageContentType(file.getContentType());
		t.setImageWidth(image.getHeight());
		t.setImageHeight(image.getHeight());
	}
	
	private static BufferedImage getBufferedImage(MultipartFile file) throws IOException {
		InputStream in = new ByteArrayInputStream(file.getBytes());
		return ImageIO.read(in);
	}
	
	private ImageUtil() {
		// hide constructor
	}
	
}
