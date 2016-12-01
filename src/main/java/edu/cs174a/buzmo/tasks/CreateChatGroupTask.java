package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class CreateChatGroupTask extends Task<Void> {
    private String chatName;
    private int duration;
    private String owner;

    public CreateChatGroupTask(String chatName, int duration, String owner) {
        this.chatName = chatName;
        this.duration = duration;
        this.owner = owner;
    }

    @Override
    protected Void call() throws Exception {
        newChatGroup();
        return null;
    }

    private void newChatGroup() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.CHATGROUPS(group_name, duration, owner) VALUES (?,?,?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.chatName);
            q.getPstmt().setInt(2, this.duration);
            q.getPstmt().setString(3, this.owner);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
