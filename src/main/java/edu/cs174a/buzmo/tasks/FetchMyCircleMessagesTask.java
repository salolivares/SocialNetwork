package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class FetchMyCircleMessagesTask extends Task<ObservableList<Message>> {
    private String email;

    public FetchMyCircleMessagesTask(String email) {
        this.email = email;
    }

    private ObservableList<Message> fetchMessages()  {
        DatabaseQuery q = null;
        ObservableList<Message> result = FXCollections.observableArrayList();

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        ResultSet rs = null;

        try {

            String sql = "SELECT * FROM ( " +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES, FRIENDS " +
                    "WHERE MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = FRIENDS.EMAIL1 " +
                    "AND FRIENDS.EMAIL2 = ? " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES, FRIENDS " +
                    "WHERE MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = FRIENDS.EMAIL2 " +
                    "AND FRIENDS.EMAIL1 = ? " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES " +
                    "WHERE MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = ? " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, WALLPOSTS " +
                    "WHERE MESSAGES.MID = WALLPOSTS.MID " +
                    "AND WALLPOSTS.RECEIVER = ? " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, WALLPOSTS " +
                    "WHERE MESSAGES.MID = WALLPOSTS.MID " +
                    "AND MESSAGES.SENDER = ?" +
                    ")" +
                    "ORDER BY TIMESTAMP DESC ";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.email);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().setString(4, this.email);
            q.getPstmt().setString(5, this.email);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                result.add(new Message(rs.getInt("mid"), rs.getString("sender"), rs.getString("body"), rs.getTimestamp("timestamp").toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }

        return result;
    }

    private void updateNumRead() {
        DatabaseQuery q = null;

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "UPDATE MESSAGES SET num_read = num_read+1 WHERE MID IN (" +
                    "SELECT MESSAGES.MID FROM (" +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES, FRIENDS " +
                    "WHERE (MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = FRIENDS.EMAIL1 " +
                    "AND FRIENDS.EMAIL2 = ?) " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES, FRIENDS " +
                    "WHERE (MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = FRIENDS.EMAIL2 " +
                    "AND FRIENDS.EMAIL1 = ?) " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, MYCIRCLEMESSAGES " +
                    "WHERE (MESSAGES.MID = MYCIRCLEMESSAGES.MID " +
                    "AND MESSAGES.SENDER = ? ))) ";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.email);
            q.getPstmt().setString(3, this.email);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }

        return;
    }

    @Override
    protected ObservableList<Message> call() throws Exception {
        updateNumRead();
        return fetchMessages();
    }
}
