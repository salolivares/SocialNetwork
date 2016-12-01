package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class SendPrivateMessageTask extends Task<Void> {
    private String email;
    private String friend;
    private String body;
    private String timestamp;
    private int numRead;

    public SendPrivateMessageTask(String email, String friend, String body, String timestamp, int numRead) {
        this.email = email;
        this.friend = friend;
        this.body = body;
        this.numRead = numRead;

        Integer sec = LocalDateTime.now().getSecond();
        this.timestamp = timestamp + ":"+ sec.toString();
        System.out.println(timestamp);
    }

    private void sendPrivateMessage() {
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

    private void insertIntoPMTable() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.PRIVATEMESSAGES(mid, receiver, flag) " +
                    "VALUES ((SELECT MID FROM MESSAGES WHERE BODY = ? AND TIMESTAMP = TO_DATE(?, 'YYYY-MM-DD HH:MI:SS') AND SENDER = ?),?,?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setString(4, this.friend);
            q.getPstmt().setInt(5, 0);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        sendPrivateMessage();
        insertIntoPMTable();
        return null;
    }
}
