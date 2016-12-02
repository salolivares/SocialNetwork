package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class SendChatGroupMessageTask extends Task<Void> {
    private String email;
    private String groupName;
    private String body;
    private String timestamp;
    private int numRead;


    public SendChatGroupMessageTask(String email, String groupName, String body, String timestamp, int numRead) {
        this.email = email;
        this.groupName = groupName;
        this.body = body;
        this.timestamp = timestamp;
        this.numRead = numRead;

        Integer sec = LocalDateTime.now().getSecond();
        this.timestamp = timestamp + ":"+ sec.toString();
        System.out.println(timestamp);
    }

    private void sendChatGroup() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }


        try {
            String sql = "INSERT INTO SALOLIVARES.MESSAGES(body, timestamp, sender, num_read) VALUES (?,TO_DATE(?, 'YYYY-MM-DD HH:MI:SS'),?,?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setInt(4, this.numRead);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoChatGroupTable() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.CHATGROUPMESSAGES(mid, chatgroupid) " +
                    "VALUES ((SELECT MID FROM MESSAGES WHERE BODY = ? AND TIMESTAMP = TO_DATE(?, 'YYYY-MM-DD HH:MI:SS') AND SENDER = ?),(SELECT chatgroupid FROM CHATGROUPS WHERE GROUP_NAME = ? ))";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setString(4, this.groupName);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        sendChatGroup();
        insertIntoChatGroupTable();
        return null;
    }
}
