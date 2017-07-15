package org.amc.game.chessserver.messaging;

import java.io.File;

class RootMailImage implements EmbeddedMailImage {

	private String contentId;
	
	private String path;
	
	private String contentType;
	
	private boolean toBeDeleted;
	
	public RootMailImage(String contentId, String path, String contentType) {
		this.contentId = contentId;
		this.path = path;
		this.contentType = contentType;
	}
	
	public RootMailImage(String contentId, String path, String contentType, boolean toBeDeleted) {
		this(contentId, path, contentType);
		this.toBeDeleted = toBeDeleted;
	}

	@Override
	public File getImageSource() {
		return new File(getPath());
	}

	@Override
	public String getContentId() {
		return contentId;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void setContentId(String contentId) {
		this.contentId = contentId;
		
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String filePath) {
		this.path = filePath;
		
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
		
	}

	@Override
	public boolean isToBeDeleted() {
		return toBeDeleted;
	}
}
