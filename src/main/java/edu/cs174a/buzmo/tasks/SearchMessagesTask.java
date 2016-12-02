package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.MainApp;
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
public class SearchMessagesTask extends Task<ObservableList<Message>> {

    private ObservableList<String> topics;
    public SearchMessagesTask(ObservableList<String> items) {
        this.topics = items;
        System.out.println(topics.get(0));
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

            String sql = "SELECT MESSAGES.* FROM MESSAGES,TOPICWORDS,MESSAGETOPICS " +
                    "WHERE MESSAGES.MID = MESSAGETOPICS.MID AND MESSAGETOPICS.TID = TOPICWORDS.TID AND " +
                    "TOPICWORDS.WORD = ?";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.topics.get(0));
            rs = q.getPstmt().executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("body"));
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
        return fetchMessages();
    }
}
