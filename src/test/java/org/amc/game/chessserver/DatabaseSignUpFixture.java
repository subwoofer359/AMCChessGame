package org.amc.game.chessserver;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;


/**
 * 
 * @author Adrian McLaughlin
 * 
 *         Create Entities required to test the different elements of the spc
 *         package
 *
 */
public class DatabaseSignUpFixture {
    private EntityManager em;
    private EntityManagerFactory factory;
    private static final String NAME = "Test Subject";
    private static final String USERNAME = "Test01";
    private static final String PASSWORD = "Hello Kitty";

    public DatabaseSignUpFixture() {

    }

    public void setUp() {
        // factory=Persistence.createEntityManagerFactory("myDataSource");
        factory = Persistence.createEntityManagerFactory("myDatabaseTest");
        EntityManagerThreadLocal.setEntityManagerFactory(factory);
        em = EntityManagerThreadLocal.getEntityManager();
        
        Player player = new HumanPlayer(NAME);
        player.setUserName(USERNAME);
        User user = new User();
        user.setName(NAME);
        user.setUserName(USERNAME);
        user.setPassword(PASSWORD.toCharArray());
        user.setPlayer(player);
        em.getTransaction().begin();
        em.persist(user);
        //em.persist(player);
        em.getTransaction().commit();
        
    }
    
    public void tearDown() {
        deleteUserPlayerTables();
        EntityManagerThreadLocal.closeEntityManager();
    }
    
    private void deleteUserPlayerTables() {
        Query q1 = em.createNativeQuery("drop table users");
        Query q2 = em.createNativeQuery("drop table players");
        //Query q3 = em.createNativeQuery("drop table authorities");
        em.getTransaction().begin();
        //q3.executeUpdate();
        q2.executeUpdate();
        q1.executeUpdate();
        em.getTransaction().commit();
    }
//
//    public void setupPartTable() throws DAOException {
//        // Clear table users
//        if (tableExists("jobtemplate")) {
//            Query q1 = em.createNativeQuery("delete from jobtemplate");
//            em.getTransaction().begin();
//            q1.executeUpdate();
//            em.getTransaction().commit();
//        }
//        // Get DAO object
//        DAO<Part> partDAO = new DAO<Part>(Part.class);
//        String[] colours = { "red", "blue", "green", "yellow" };
//        String[] companies = { "HMV", "Granada", "Apple", "Apple" };
//        String[] names = { "CD", "Car", "IPOD", "IPHONE" };
//        String[] part_ids = { "393939", "99w2933", "ap3003", "ap2202" };
//        String[] qss_nos = { "CD 001", "GA 002", "A 001", "A 002" };
//
//        Part part = null;
//        for (int i = 0; i < colours.length; i++) {
//            part = new Part();
//            part.setName(names[i]);
//            part.setColour(colours[i]);
//            part.setCompany(companies[i]);
//            part.setExternal(true);
//            part.setPart_id(part_ids[i]);
//            part.setQss_no(qss_nos[i]);
//            part.setRevision("0.2" + i);
//            part.setVersion(String.valueOf(i));
//            partDAO.addEntity(part);
//        }
//        // Set it to be garbage collected
//        partDAO = null;
//    }
//
//    public void setUpMaterialTable() throws DAOException {
//        // Delete moulding process table
//        if (tableExists("material")) {
//            Query q1 = em.createNativeQuery("delete from material");
//            em.getTransaction().begin();
//            q1.executeUpdate();
//            em.getTransaction().commit();
//        }
//        DAO<Material> materialDAO = new DAO<Material>(Material.class);
//        String[] company = { "Exxon Mobil" };
//        String[] type = { "PC" };
//        String[] name = { "Moplen 540P" };
//
//        for (int i = 0; i < company.length; i++) {
//            Material material = new Material();
//            material.setCompany(company[i]);
//            material.setType(type[i]);
//            material.setName(name[i]);
//
//            materialDAO.addEntity(material);
//        }
//    }
//
//    /**
//     * A Material table is set up when calling this method
//     * 
//     * @throws DAOException
//     */
//    public void setUpMouldingProcessTable() throws DAOException {
//        // Delete moulding process table
//        if (tableExists("processSheets")) {
//            Query q1 = em.createNativeQuery("delete from processSheets");
//            em.getTransaction().begin();
//            q1.executeUpdate();
//            em.getTransaction().commit();
//        }
//
//        setUpMaterialTable();
//        DAO<Material> materialDAO = new DAO<Material>(Material.class);
//        Material material = materialDAO.findEntities().get(0);
//
//        DAO<MouldingProcess> mpDAO = new DAO<MouldingProcess>(MouldingProcess.class);
//
//        String[] partId = { "1", "2", "3", "4" };
//        int[] machineSize = { 150, 100, 125, 75 };
//        String[] machineNo = { "Fanuc 1", "Fanuc 7", "Fanuc 5", "Boy 2" };
//        int[] materials = { material.getId(), material.getId(), material.getId(), material.getId() };
//        String[] masterbatchNo = { "20303", "030030", "303002", "30302123" };
//        Date[] dateOfIssue = { Date.valueOf("2014-06-12"), Date.valueOf("2013-05-11"),
//                Date.valueOf("2010-02-12"), Date.valueOf("2004-01-02") };
//        String signOffBy = "John Malone";
//
//        for (int i = 0; i < partId.length; i++) {
//            MouldingProcess mp = new MouldingProcess();
//            mp.setPartId(partId[i]);
//            mp.setMachineSize(machineSize[i]);
//            mp.setMachineNo(machineNo[i]);
//            mp.setMaterial(materials[i]);
//            mp.setMasterbatchNo(masterbatchNo[i]);
//            mp.setDateOfIssue(dateOfIssue[i]);
//            mp.setSignOffBy(signOffBy);
//            mpDAO.addEntity(mp);
//        }
//
//        mpDAO = null;
//
//    }
//
//    public void setUpUserTable() throws DAOException {
//        // Clear table users
//        if (tableExists("users")) {
//            Query q1 = em.createNativeQuery("delete from users");
//            em.getTransaction().begin();
//            q1.executeUpdate();
//            em.getTransaction().commit();
//        }
//        // Get DAO object
//        DAO<User> userDAO = new DAO<User>(User.class);
//
//        // Create User Entities and add them to the database
//        String[] fullnames = { "Adrian McLaughlin", "Stephen Nolan", "Chris Dalton" };
//        String[] userNames = { "adrian", "stephen", "chris" };
//        String[] emailAddresses = { "adrian@adrianmclaughlin.ie", "stephen@gmail.com",
//                "chris@gmail.com" };
//        String[] passwords = { "wewew", "3i3i3i3i", "fpefpefle" };
//        User user = null;
//        for (int i = 0; i < fullnames.length; i++) {
//            user = new User();
//            user.setActive(true);
//            user.setEmailAddress(emailAddresses[i]);
//            user.setFullName(fullnames[i]);
//            user.setUserName(userNames[i]);
//            user.setPassword(passwords[i]);
//            userDAO.addEntity(user);
//        }
//        // Set it to be garbage collected
//        userDAO = null;
//    }
//
//    public void setUpSPCMeasurementTable() throws DAOException {
//        // Clear SPCDimension Table
//        if (tableExists("SPCMeasurements")) {
//            Query q1 = em.createNativeQuery("delete from SPCMeasurements");
//            em.getTransaction().begin();
//            q1.executeUpdate();
//            em.getTransaction().commit();
//        }
//
//        DAO<Part> partDAO = new DAO<Part>(Part.class);
//        SPCMeasurementDAO measurementDAO = new SPCMeasurementDAO();
//
//        // Presuming there are entries in the part database table;
//        Part p = partDAO.findEntities().get(0);
//
//        String[] dimensions = { "length", "height", "width", "radius" };
//        float[] lowerLimits = { 0.3f, 0.2f, 0f, 3f, 1f };
//        float[] upperLimits = { 0.3f, 0.2f, 0f, 3f, 1f };
//        float[] nominals = { 123.34f, 221.2f, 11.22f, 21.23f, 0.33f };
//        int[] noOfMeasurments = { 3, 2, 5, 5, 10 };
//        // String[]
//        // tableIds={"tableId0020202020","tableId03","tableId0020ffrrf0","tableId022ff202020","tableId0020330"};
//
//        for (int i = 0; i < dimensions.length; i++) {
//            SPCMeasurement measurement = new SPCMeasurement();
//            measurement.setActive(true);
//            measurement.setDimension(dimensions[i]);
//            measurement.setLowerLimit(lowerLimits[i]);
//            measurement.setUpperLimit(upperLimits[i]);
//            measurement.setNominal(nominals[i]);
//            measurement.setNoOfMeasurements(noOfMeasurments[i]);
//            // measurement.setTableId(tableIds[i]);
//            measurement.setPart(p);
//            measurementDAO.addEntity(measurement);
//        }
//
//    }
//
//    public boolean tableExists(String tableName) {
//        Query tableExists = em.createNativeQuery("SHOW TABLES");
//        em.getTransaction().begin();
//        tableExists.executeUpdate();
//        List<String> tables = tableExists.getResultList();
//        boolean result = false;
//        if (tables.contains(tableName)) {
//            result = true;
//        }
//        em.getTransaction().commit();
//
//        return result;
//    }
//    
//
//    // Todo Create method setUpSPCMeasurementTable
}
