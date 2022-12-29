package by.tms.project.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Logger log = LogManager.getLogger(ConnectionManager.class);

    private static String url = "jdbc:mysql://localhost:3306/order_service";
    private static String user = "root";
    private static String password = "root";

    private ConnectionManager(){}

    public static Connection open(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            log.error("Exception during DB connection: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
