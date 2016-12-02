package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class SendFriendMyCircleMessageTask extends Task<Void> {
    private String email;
    private String body;
    private String timestamp;
    private int numRead;
    private ObservableList<String> topics;
    private ObservableList<String> friends;

    public SendFriendMyCircleMessageTask(String email, String body, String timestamp, int numRead, ObservableList<String> topics, ObservableList<String> friends) {
        this.email = email;
        this.body = body;
        this.numRead = numRead;
        this.topics = topics;
        this.friends = friends;

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

    private void insertIntoWallPostTable(String friend) {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO wallposts(mid, receiver) " +
                    "VALUES ((SELECT MID FROM MESSAGES WHERE BODY = ? AND TIMESTAMP = TO_DATE(?, 'YYYY-MM-DD HH:MI:SS') AND SENDER = ?), ?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setString(4, friend);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertMessageTopic(String topic) {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO messagetopics(mid, tid) " +
                    "VALUES ((SELECT MID FROM MESSAGES WHERE BODY = ? AND TIMESTAMP = TO_DATE(?, 'YYYY-MM-DD HH:MI:SS') AND SENDER = ?), " +
                    "(SELECT TID FROM TOPICWORDS WHERE WORD = ?))";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.body);
            q.getPstmt().setString(2, this.timestamp);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setString(4, topic);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {
        sendMessage();
        friends.forEach(this::insertIntoWallPostTable);
        topics.forEach(this::insertMessageTopic);
        return null;
    }
}
