package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.ChatGroup;
import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchChatGroupsTask extends Task<ObservableList<ChatGroup>> {
    private String email;

    public FetchChatGroupsTask(String email) {
        this.email = email;
    }

    @Override
    protected ObservableList<ChatGroup> call() throws Exception {
        return fetchChatGroups();
    }

    private ObservableList<ChatGroup> fetchChatGroups() {
        DatabaseQuery q = null;
        ObservableList<ChatGroup> result = FXCollections.observableArrayList();

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        String sql = "SELECT * FROM GROUPMEMBER, CHATGROUPS WHERE GROUPMEMBER.CHATGROUPID = CHATGROUPS.CHATGROUPID AND EMAIL = ?";
        ResultSet rs = null;
        try {
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            rs = q.getPstmt().executeQuery();

            while(rs.next()){
                String groupName = rs.getString("group_name");
                int duration = rs.getInt("duration");
                String owner = rs.getString("owner");
                result.add(new ChatGroup(groupName, duration, owner));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }

        return result;
    }
}
