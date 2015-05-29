package org.amc.util.web;

import org.apache.log4j.Logger;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @author Adrian Mclaughlin
 * @version 1
 */
public final class StartupShutdownListener implements ServletContextListener {

    public final static Logger log = Logger.getLogger(StartupShutdownListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("Servlet shut down....");
        deregisterDatabaseDrivers();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // Setting up logger
        // System.out.println(arg0.getServletContext().getRealPath("/")+"WEB-INF/log4j.properties");
        // PropertyConfigurator.configure("log4j.properties");
        log.info("Servlet started up....");
    }

    /**
     * When the application is stopped JDBC drivers ared deregistered to help
     * prevent a memory leak
     */
    public void deregisterDatabaseDrivers() {
        Enumeration<Driver> list = DriverManager.getDrivers();
        while (list.hasMoreElements()) {
            Driver driver = list.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.debug(driver.toString() + " deregistered");
            } catch (Exception e) {
                log.debug("In attempt to deregister driver:" + driver + " an exception has thrown:"
                                + e.getMessage());
            }
        }
        /*
         * Force closing down of the MySQL Abandoned connection cleanup thread
         * solution found on website:
         * http://stackoverflow.com/questions/11872316
         * /tomcat-guice-jdbc-memory-leak
         */
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (Thread t : threadArray) {
            if (t.getName().contains("Abandoned connection cleanup thread")) {
                synchronized (t) {
                    try {
                        AbandonedConnectionCleanupThread.shutdown();
                    } catch (InterruptedException ie) {
                        log.warn("Problem stopping AbandonedConnectionCleanupThread:"
                                        + ie.getMessage());
                    }

                }
            }
        }
    }
}
