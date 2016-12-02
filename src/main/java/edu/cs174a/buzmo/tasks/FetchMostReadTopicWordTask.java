package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.MessageWithTopic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class FetchMostReadTopicWordTask extends Task<ObservableList<MessageWithTopic>> {

    public FetchMostReadTopicWordTask() {}

    private ObservableList<MessageWithTopic> fetchMessages()  {
        DatabaseQuery q = null;
        ObservableList<MessageWithTopic> result = FXCollections.observableArrayList();

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
                    "WHERE TopicWords.tid = messagetopics.tid AND MESSAGES.mid = MESSAGETOPICS.mid";
            q.pQuery(sql);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                result.add(new MessageWithTopic(rs.getInt("mid"), rs.getString("sender"), rs.getString("body"), rs.getTimestamp("timestamp").toString(), rs.getString("word")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }

        return result;
    }

    @Override
    protected ObservableList<MessageWithTopic> call() throws Exception {
        return fetchMessages();
    }
}
