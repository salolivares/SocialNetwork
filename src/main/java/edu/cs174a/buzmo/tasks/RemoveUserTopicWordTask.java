package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class RemoveUserTopicWordTask extends Task<Void> {
    private TopicWord topicWord;
    private String email;

    public RemoveUserTopicWordTask(String email, TopicWord toDelete) {
        this.email = email;
        this.topicWord = toDelete;
    }

    @Override
    protected Void call() throws Exception {
        removeUserTopicWord();
        return null;
    }

    private void removeUserTopicWord() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "DELETE FROM SALOLIVARES.USERTOPICS WHERE EMAIL = ? AND TID = ?";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            q.getPstmt().setString(2, this.topicWord.getTid());
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
