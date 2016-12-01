package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class AcceptUserToChatGroupTask extends Task<Void> {
    private String groupName;
    private String email;

    public AcceptUserToChatGroupTask(String groupName, String email) {
        this.groupName = groupName;
        this.email = email;
    }

    private void acceptUser() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "UPDATE GROUPMEMBER SET MEMBER_STATUS = 1 WHERE EMAIL = ? AND CHATGROUPID = (SELECT chatgroupid FROM CHATGROUPS WHERE GROUP_NAME = ?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.groupName);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        acceptUser();
        return null;
    }
}
