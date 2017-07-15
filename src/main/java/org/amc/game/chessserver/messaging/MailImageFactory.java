package org.amc.game.chessserver.messaging;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

class MailImageFactory implements ServletContextAware {

	private ServletContext servletContext;
	
	static final String ERROR_NO_CONTEXT = "%s: ServletContext has not been set";

	public EmbeddedMailImage getTempServletPathImage(String contentId, String path, String contentType) {
		checkServletContext();
		EmbeddedServletImage image = new EmbeddedServletImage(contentId, path, contentType, true);
		image.setServletContext(servletContext);

		return image;
	}
	
	private void checkServletContext() {
		if (servletContext == null) {
			throw new NullPointerException(String.format(ERROR_NO_CONTEXT, MailImageFactory.class.getSimpleName()));
		}
	}

	public EmbeddedMailImage getServletPathImage(String contentId, String path, String contentType) {
		checkServletContext();
		EmbeddedServletImage image = new EmbeddedServletImage(contentId, path, contentType);
		image.setServletContext(servletContext);

		return image;
	}

	public EmbeddedMailImage getTempRootPathImage(String contentId, String path, String contentType) {
		return new RootMailImage(contentId, path, contentType, true);
	}

	public EmbeddedMailImage getRootPathImage(String contentId, String path, String contentType) {
		return new RootMailImage(contentId, path, contentType);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
