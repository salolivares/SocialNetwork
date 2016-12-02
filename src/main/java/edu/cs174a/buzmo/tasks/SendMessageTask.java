package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.Message;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class SendMessageTask extends Task<Void> {
    private String email;
    private String body;
    private String timestamp;
    private int numRead;
    private int pub;

    public SendMessageTask(String email, String body, String timestamp, int numRead, int pub) {
        this.email = email;
        this.body = body;
        this.numRead = numRead;
        this.pub = pub;

        Integer sec = LocalDateTime.now().getSecond();
        this.timestamp = timestamp + ":"+ sec.toString();
        System.out.println(this.timestamp);
    }

    private void sendMessage() {
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

    private void insertIntoMyCircleTable() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO mycirclemessages(mid, ispublic) " +
                    "VALUES ((SELECT MID FROM MESSAGES WHERE BODY = ? AND TIMESTAMP = TO_DATE(?, 'YYYY-MM-DD HH:MI:SS') AND SENDER = ?), ?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setInt(4, this.pub);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        sendMessage();
        insertIntoMyCircleTable();
        return null;
    }
}
