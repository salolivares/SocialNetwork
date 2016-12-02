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
public class FetchMostReadTopicWordTask extends Task<ObservableList<Message>> {

    public FetchMostReadTopicWordTask() {}

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

            String sql = "SELECT UNIQUE topicwords.word, messages.mid, messages.body, messages.sender, messages.timestamp " +
                    "FROM MessageTopics, Messages, TopicWords " +
                    "WHERE TopicWords.tid = messagetopics.tid AND MESSAGES.mid = MESSAGETOPICS.mid;";
            q.pQuery(sql);
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
        return fetchMessages();
    }
}
