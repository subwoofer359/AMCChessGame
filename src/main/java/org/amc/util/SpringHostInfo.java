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
        String builder = HTTP +
                hostname +
                ':' +
                String.valueOf(port) +
                servletContext.getContextPath();

        return builder;
    }
}
