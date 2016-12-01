package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;
import java.util.Observable;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class CreateTopicTask extends Task<Void> {
    private String word;


    public CreateTopicTask(String text) {
        this.word = text;
    }

    @Override
    protected Void call() throws Exception {
        newTopicWord();
        return null;
    }

    private void newTopicWord() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.TOPICWORDS(word) VALUES (?)";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.word);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
