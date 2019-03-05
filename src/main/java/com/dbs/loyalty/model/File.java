package com.dbs.loyalty.model;

import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.config.Constant;

public class File {

	private String id;
	
	private byte[] imageBytes;
	
	private MultipartFile file;

	public File() {
		this.id = Constant.ZERO;
	}
	
	public File(String id, byte[] imageBytes) {
		this.id = id;
		this.imageBytes = imageBytes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
