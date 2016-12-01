package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class RejectFriendTask extends Task<Void> {
    private String email1;
    private String email2;

    public RejectFriendTask(String email1, String email2) {
        this.email1=email1; this.email2=email2;
    }

    @Override
    protected Void call() throws Exception {
        rejectFriendRequest();
        return null;
    }

    private void rejectFriendRequest() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "DELETE FROM FRIENDS WHERE EMAIL1 = ? AND EMAIL2 = ?";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email1);
            q.getPstmt().setString(2, this.email2);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
