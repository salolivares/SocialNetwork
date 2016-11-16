package edu.cs174a.buzmo.util;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class DatabaseConnection {
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
    private static final String USER = "salolivares";
    private static final String PASS = "480";

    private Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection != null) {
            return connection;
        }

        Class.forName(JDBC_DRIVER);
        connection =  DriverManager.getConnection(DB_URL, USER, PASS);
        return connection;
    }

    public void closeConnection() throws SQLException {
        DbUtils.close(connection);
    }
}
