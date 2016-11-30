package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class FetchUsersTask extends Task<ObservableList<String>> {

    private String email;
    private String topics;
    private Integer numMessages;
    private Integer numDays;

    public FetchUsersTask(String email, String topics, String numMessages, Integer numDays) {
        this.email = email;
        this.topics = topics;
        if (!numMessages.isEmpty())
            this.numMessages = Integer.parseInt(numMessages);
        else
            this.numMessages = 0;
        this.numDays = numDays;
    }

    private ObservableList<String> fetchUsers()  {
        DatabaseQuery q = null;
        ObservableList<String> result = FXCollections.observableArrayList();

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        ResultSet rs = null;

        try {
            if(topics.isEmpty() && numMessages == 0) {
                String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ?";
                q.pQuery(sql);
                q.getPstmt().setString(1, this.email);
            } else if (numMessages == 0){
                String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ? " +
                        "UNION SELECT EMAIL FROM USERTOPICS, TOPICWORDS " +
                        "WHERE TOPICWORDS.WORD = ? AND TOPICWORDS.TID = USERTOPICS.TID";
                q.pQuery(sql);
                q.getPstmt().setString(1, this.email);
                q.getPstmt().setString(2, this.topics);
            }
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                String email = rs.getString("email");
                result.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }


        return result;
    }

    @Override
    protected ObservableList<String> call() throws Exception {
        return fetchUsers();
    }
}
