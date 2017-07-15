package org.amc.game.chessserver.messaging;

import java.io.File;

import javax.mail.internet.MimeMessage;

/**
 * Encapsulates information of an image to be embedding in
 * a {@link MimeMessage}
 * @author Adrian Mclaughlin
 *
 */
public interface EmbeddedMailImage {

	public String getContentId();

    public File getImageSource();

    public String getContentType();

    public void setContentId(String contentId);
    
    public String getPath();

    public void setPath(String filePath);

    public void setContentType(String contentType);

    public boolean isToBeDeleted();
}
