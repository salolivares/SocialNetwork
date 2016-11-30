package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class AddFriendTask extends Task<Void> {

    private String email1;
    private String email2;

    public AddFriendTask(String email, String email2) {
        email1 = email; this.email2 = email2;
    }

    private void addFriend() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.FRIENDS(email1,email2,request_status) VALUES (?,?,?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email1);
            q.getPstmt().setString(2, this.email2);
            q.getPstmt().setInt(3, 0);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        addFriend();
        return null;
    }
}
