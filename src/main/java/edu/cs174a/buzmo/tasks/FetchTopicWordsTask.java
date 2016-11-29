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

    private ObservableList<TopicWord> fetchUserTopicWords() throws SQLException, ClassNotFoundException {
        DatabaseQuery q = new DatabaseQuery();
        ObservableList<TopicWord> result = FXCollections.observableArrayList();
        String sql = "SELECT * FROM USERTOPICS, TOPICWORDS WHERE USERTOPICS.TID = TOPICWORDS.TID AND USERTOPICS.EMAIL = " + "'" + this.email + "'";
        System.out.println(sql);
        ResultSet rs = q.query(sql);

        while(rs.next()){
            String tid = rs.getString("USERTOPICS.TID");
            String word = rs.getString("WORD");
            result.add(new TopicWord(tid, word));
        }

        //Important!! Release resources.
        q.close();

        return result;
    }
}
