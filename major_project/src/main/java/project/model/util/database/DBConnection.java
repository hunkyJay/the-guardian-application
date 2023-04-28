package project.model.util.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An util class to help for database connection
 */
public class DBConnection {
    private static final String dbName = "project.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    private static DBConnection dBConnectionSingle  = null;

    /**
     * Apply single pattern to get the DBConnection util instance for outside entries
     * @return DBConnection return the DBConnection util instance
     */
    public static DBConnection getInstance(){
        if(dBConnectionSingle == null){
            //Prevent threads concurrency
            synchronized (DBConnection.class){
                if(dBConnectionSingle == null){
                    dBConnectionSingle = new DBConnection();
                }
            }
        }
        return dBConnectionSingle;
    }

    /**
     * Basic private constructor for DBConnection to be used for the singleton pattern
     */
    private DBConnection(){
        createDB();
    }

    /**
     * Create database if not exist
     */
    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
//            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            System.out.println("Database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    /**
//     * Remove the existing database
//     */
//    public void removeDB() {
//        File dbFile = new File(dbName);
//        if (dbFile.exists()) {
//            boolean result = dbFile.delete();
//            if (!result) {
//                System.out.println("Couldn't delete existing db file");
//                System.exit(-1);
//            } else {
//                System.out.println("Removed existing DB file.");
//            }
//        } else {
//            System.out.println("No existing DB file.");
//        }
//    }

    /**
     * Get connection to the database
     * @return Connection return the connection object to the database
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(dbURL);

        return connection;
    }

}
