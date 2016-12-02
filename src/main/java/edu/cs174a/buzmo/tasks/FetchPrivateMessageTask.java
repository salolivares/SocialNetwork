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
public class FetchPrivateMessageTask extends Task<ObservableList<Message>> {
    private String email;
    private String friend;

    public FetchPrivateMessageTask(String email, String friend) {
        this.email = email; this.friend = friend;
        System.out.println("email is: " + email + " friend is: " + friend);
    }

    private ObservableList<Message> fetchPrivateMessages()  {
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

            String sql = "SELECT * FROM " +
                    "(SELECT MESSAGES.* FROM MESSAGES, PRIVATEMESSAGES " +
                    "WHERE MESSAGES.mid = PRIVATEMESSAGES.mid " +
                    "AND MESSAGES.sender = ? " +
                    "AND PRIVATEMESSAGES.receiver = ? " +
                    "AND (PRIVATEMESSAGES.flag = 0 OR PRIVATEMESSAGES.flag = 2) " +
                    "UNION " +
                    "SELECT MESSAGES.* FROM MESSAGES, PRIVATEMESSAGES " +
                    "WHERE MESSAGES.mid = PRIVATEMESSAGES.mid " +
                    "AND MESSAGES.sender = ? " +
                    "AND PRIVATEMESSAGES.receiver = ? " +
                    "AND (PRIVATEMESSAGES.flag = 0 OR PRIVATEMESSAGES.flag = 1)) " +
                    "ORDER BY timestamp ASC";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.friend);
            q.getPstmt().setString(3, this.friend);
            q.getPstmt().setString(4, this.email);
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

    @Override
    protected ObservableList<Message> call() throws Exception {
        return fetchPrivateMessages();
    }
}
