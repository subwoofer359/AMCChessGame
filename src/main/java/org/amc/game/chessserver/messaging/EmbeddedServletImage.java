package org.amc.game.chessserver.messaging;

import java.io.File;

import javax.servlet.ServletContext;

class EmbeddedServletImage implements EmbeddedMailImage {

    private String contentId;
    
    private String path;
    
    private File imageSource;
    
    private String contentType;
    
    private boolean toBeDeleted;
    
    private ServletContext servletContext;
    
    EmbeddedServletImage(String contentId, String path, String contentType) {
        this.contentId = contentId;
        this.path = path;
        this.contentType = contentType;
    }
    
    EmbeddedServletImage(String contentId, String path, String contentType, boolean toBeDeleted) {
        this(contentId, path, contentType);
        this.toBeDeleted = toBeDeleted;
    }

    public String getContentId() {
        return contentId;
    }
    
    /**
     * 
     * @return a File or null
     */
    public File getImageSource() {
    	imageSource = new File(servletContext.getRealPath(path));
        return imageSource;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String filePath) {
        this.path = filePath;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
