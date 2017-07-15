package org.amc.util.web;

import org.apache.batik.util.CleanerThread;
import org.apache.log4j.Logger;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import java.lang.reflect.Field;
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
        stopBatikCleanerThread();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // Setting up logger
        // System.out.println(arg0.getServletContext().getRealPath("/")+"WEB-INF/log4j.properties");
        // PropertyConfigurator.configure("log4j.properties");
    	String hostName = System.getenv("HOSTIP");
    	arg0.getServletContext().setAttribute("HOSTIP", hostName);
    	log.info("Storing HOSTNAME:" + hostName);
        log.info("Servlet started up....");
    }

    private void stopBatikCleanerThread() {
        try {
            
            Field[] fields = CleanerThread.class.getDeclaredFields();
            for(Field field: fields) {
                log.debug(field.getName()+"-"+field.getType()+"-"+field.toGenericString());
            }
            Field cleanerThread = CleanerThread.class.getDeclaredField("thread");
            
            cleanerThread.setAccessible(true);
            Thread thread = (Thread) cleanerThread.get(null);
            cleanerThread.set(null, null);
            if(thread == null) {
                log.debug("CleanerThread: Thread is null");
            } else {
                thread.interrupt();
                log.debug("CleanerThread: interrupting");
                try {
                    thread.join(20000);
                } catch (InterruptedException e) {
                    log.debug("CleanerThread: Didn't complete within 2 seconds");
                }
                if (thread.isAlive()) {
                    log.debug("CleanerThread: killed");
                    thread.stop();
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

    }


    /**
     * When the application is stopped JDBC drivers ared deregistered to help
     * prevent a memory leak
     */
    private void deregisterDatabaseDrivers() {
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
