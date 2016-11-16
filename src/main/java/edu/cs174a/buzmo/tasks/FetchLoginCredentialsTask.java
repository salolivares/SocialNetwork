package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseConnection;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.SQLException;

public class FetchLoginCredentialsTask extends Task<Boolean> {
    private String user;
    private String pass;
    private DatabaseConnection databaseConnection;
    private Connection conn;

    public FetchLoginCredentialsTask(String user, String pass) {
        this.databaseConnection = new DatabaseConnection();
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected Boolean call() throws Exception {
        return fetchLoginCredentials();
    }

    private Boolean fetchLoginCredentials() throws SQLException, ClassNotFoundException {
        databaseConnection.getConnection();
        Boolean result = "sal".equals(user) && "123".equals(pass);
        databaseConnection.closeConnection();
        return result;
    }
}
