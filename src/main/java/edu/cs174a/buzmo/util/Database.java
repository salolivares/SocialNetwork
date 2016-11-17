package edu.cs174a.buzmo.util;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

/** Singleton **/
public class Database {
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
    private static final String USER = "salolivares";
    private static final String PASS = "480";

    private static Database db;
    private static Connection connection;

    private Database() { }

    public static Database getInstance(){
        if(db == null){
            db = new Database();
        }

        return db;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection == null || connection.isClosed()) {
            Class.forName(JDBC_DRIVER);
            connection =  DriverManager.getConnection(DB_URL, USER, PASS);
        }

        return connection;
    }

    public void closeConnection() throws SQLException {
        DbUtils.close(connection);
    }
}
