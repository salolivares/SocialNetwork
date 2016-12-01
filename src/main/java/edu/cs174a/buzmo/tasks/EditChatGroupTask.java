package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class EditChatGroupTask extends Task<Void> {
    private String oldName;
    private String groupName;
    private int duration;

    public EditChatGroupTask(String name, String groupName, int duration) {
        this.oldName = name;
        this.groupName = groupName;
        this.duration = duration;
    }

    private void editChatGroup() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "UPDATE CHATGROUPS SET GROUP_NAME = ?, DURATION = ? WHERE GROUP_NAME = ?";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.groupName);
            q.getPstmt().setInt(2, this.duration);
            q.getPstmt().setString(3, this.oldName);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        editChatGroup();
        return null;
    }
}
