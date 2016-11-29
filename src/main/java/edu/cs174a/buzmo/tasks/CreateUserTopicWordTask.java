package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class CreateUserTopicWordTask extends Task<Void> {
    private String email;
    private String word;

    public CreateUserTopicWordTask(String email, String word) {
        this.email = email;
        this.word = word;
    }

    @Override
    protected Void call() throws Exception {
        newTopicWord();
        newUserTopicWord();
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

    private void newUserTopicWord()  {
        DatabaseQuery q = null;

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO SALOLIVARES.USERTOPICS(EMAIL, TID) VALUES (?, (SELECT TID FROM TOPICWORDS WHERE TOPICWORDS.WORD = ?))";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.word);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
