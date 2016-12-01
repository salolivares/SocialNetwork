package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class InviteUserToChatGroupTask extends Task<Void> {
    private String groupName;
    private String email;

    public InviteUserToChatGroupTask(String groupName, String email) {
        this.groupName = groupName;
        this.email = email;
    }

    private void inviteUser() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.GROUPMEMBER(email,group_name,member_status) VALUES (?,?,?)";
            q.pQuery(sql);
            q.getPstmt().setString(2, this.groupName);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setInt(3, 0);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        inviteUser();
        return null;
    }
}
