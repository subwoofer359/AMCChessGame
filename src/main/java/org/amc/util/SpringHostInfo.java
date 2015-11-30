package org.amc.util;

import javax.servlet.ServletContext;

public class SpringHostInfo {

    private String hostname;
    
    private int port;
    
    private ServletContext servletContext;
    
    static final String HTTP = "http://";
    
    public SpringHostInfo(ServletContext servletContext, String hostname, int port) {
        this.servletContext = servletContext;
        this.hostname = hostname;
        this.port = port;
    }
    
    public String getHostUrl() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(HTTP);
        builder.append(hostname);
        builder.append(':');
        builder.append(String.valueOf(port));
        builder.append(servletContext.getContextPath());
        return builder.toString();
    }
}
