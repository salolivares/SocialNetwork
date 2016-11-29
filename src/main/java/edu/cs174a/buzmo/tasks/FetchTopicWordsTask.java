package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchTopicWordsTask extends Task<ObservableList<TopicWord>> {
    private String email;

    public FetchTopicWordsTask(String email) {
        this.email = email;
    }

    @Override
    protected ObservableList<TopicWord> call() throws Exception {
        return fetchUserTopicWords();
    }

    private ObservableList<TopicWord> fetchUserTopicWords()  {
        DatabaseQuery q = null;
        ObservableList<TopicWord> result = FXCollections.observableArrayList();

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        String sql = "SELECT * FROM USERTOPICS, TOPICWORDS WHERE USERTOPICS.TID = TOPICWORDS.TID AND USERTOPICS.EMAIL = " + "'" + this.email + "'";
        ResultSet rs = null;

        try {
            rs = q.query(sql);
            while(rs.next()){
                String tid = rs.getString("tid");
                String word = rs.getString("word");
                result.add(new TopicWord(tid, word));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }


        return result;
    }
}
