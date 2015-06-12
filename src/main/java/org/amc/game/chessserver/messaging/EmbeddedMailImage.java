package org.amc.game.chessserver.messaging;

import java.io.File;


class EmbeddedMailImage {

    private String contentId;
    private File filePath;
    private String contentType;
    private boolean toBeDeleted;
    
    EmbeddedMailImage(String contentId, File filePath, String contentType) {
        this.contentId = contentId;
        this.filePath = filePath;
        this.contentType = contentType;
        this.toBeDeleted = true;
    }
    
    EmbeddedMailImage(String contentId, File filePath, String contentType, boolean toBeDeleted) {
        this.contentId = contentId;
        this.filePath = filePath;
        this.contentType = contentType;
        this.toBeDeleted = toBeDeleted;
    }

    public String getContentId() {
        return contentId;
    }

    public File getImageSource() {
        return filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    public void setToBeDeleted(boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }
    
    

}
