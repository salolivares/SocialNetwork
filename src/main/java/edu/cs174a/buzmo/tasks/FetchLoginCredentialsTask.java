package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FetchLoginCredentialsTask extends Task<Boolean> {
    private String user;
    private String pass;

    public FetchLoginCredentialsTask(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected Boolean call() throws Exception {
        return fetchLoginCredentials();
    }

    private Boolean fetchLoginCredentials() throws SQLException, ClassNotFoundException {
        DatabaseQuery q = new DatabaseQuery();
        Boolean result = false;
        ResultSet rs = q.query("SELECT email, password FROM SALOLIVARES.Users");

        while(rs.next()){
            String id  = rs.getString("email");
            String age = rs.getString("password");

            if (id.equals(user) && age.equals(pass)){
                result = true;
                break;
            } else if (user.equals("sal")) { //TODO: REMOVE ME WHEN DONE TESTING
                result = true;
                break;
            }
        }

        //Important!! Release resources.
        q.close();

        return result;
    }
}
