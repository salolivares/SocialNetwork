package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class CreateAccountTask extends Task<Void> {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String screenname;

    public CreateAccountTask(String name, String email, String password, String phone, String screenname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.screenname = screenname;
    }

    @Override
    protected Void call() throws Exception {
        newAccount();
        return null;
    }

    private void newAccount() throws SQLException, ClassNotFoundException {
        DatabaseQuery q = new DatabaseQuery();
        q.pQuery("INSERT INTO SALOLIVARES.USERS (NAME, EMAIL, PASSWORD, PHONE, SCREEN_NAME, IS_MANAGER) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
        q.getPstmt().setString(1, name);
        q.getPstmt().setString(2, email);
        q.getPstmt().setString(3, password);
        q.getPstmt().setString(4, phone);
        q.getPstmt().setString(5, screenname);
        q.getPstmt().setInt(6,0);
        q.getPstmt().executeUpdate();
        q.close();
    }
}
