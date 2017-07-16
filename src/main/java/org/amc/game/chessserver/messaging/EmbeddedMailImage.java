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

	String getContentId();

    File getImageSource();

    String getContentType();

    void setContentId(String contentId);
    
    String getPath();

    void setPath(String filePath);

    void setContentType(String contentType);

    boolean isToBeDeleted();
}
