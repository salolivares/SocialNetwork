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
    private String choice;
    private String user;

    public FetchUsersTask(String user, String email, String topics, String numMessages, Integer numDays, String choice) {
        this.user = user;
        this.email = email;
        this.topics = topics;
        if (!numMessages.isEmpty())
            this.numMessages = Integer.parseInt(numMessages);
        else
            this.numMessages = 0;
        this.numDays = numDays;
        this.choice = choice;
        System.out.println("Choice is: " + choice);
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

        if (this.choice.equals("All Users")) {
            try {
                if (topics.isEmpty()) {
                    String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ?";
                    q.pQuery(sql);
                    q.getPstmt().setString(1, this.email);
                } else {
                    String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ? " +
                            "UNION SELECT EMAIL FROM USERTOPICS, TOPICWORDS " +
                            "WHERE TOPICWORDS.WORD = ? AND TOPICWORDS.TID = USERTOPICS.TID";
                    q.pQuery(sql);
                    q.getPstmt().setString(1, this.email);
                    q.getPstmt().setString(2, this.topics);
                }
                rs = q.getPstmt().executeQuery();
                while (rs.next()) {
                    String email = rs.getString("email");
                    result.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                q.close();
            }
        } else {
            try {
                if (topics.isEmpty()) {
                    String sql = "SELECT EMAIL FROM USERS, FRIENDS WHERE USERS.EMAIL = ? " +
                            "AND ((FRIENDS.EMAIL1 = ? AND FRIENDS.EMAIL2 = ?) " +
                            "OR (FRIENDS.EMAIL1 = ? AND FRIENDS.EMAIL2 = ?))";
                    q.pQuery(sql);
                    q.getPstmt().setString(1, this.email);
                    q.getPstmt().setString(2, this.email);
                    q.getPstmt().setString(3, this.user);
                    q.getPstmt().setString(4, this.user);
                    q.getPstmt().setString(5, this.email);
                } else {
//                    String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ? " +
//                            "UNION SELECT EMAIL FROM USERTOPICS, TOPICWORDS, FRIENDS " +
//                            "WHERE TOPICWORDS.WORD = ? AND TOPICWORDS.TID = USERTOPICS.TID AND EMAIL != ? AND " +
//                            "((FRIENDS.EMAIL1 = ? AND FRIENDS.EMAIL2 = ?) " +
//                            "OR (FRIENDS.EMAIL1 = ? AND FRIENDS.EMAIL2 = ?))";

                    String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = ? " +
                            "UNION SELECT EMAIL1 FROM FRIENDS, USERTOPICS, TOPICWORDS " +
                            "WHERE EMAIL1 = USERTOPICS.EMAIL AND TOPICWORDS.WORD = ? AND TOPICWORDS.TID = USERTOPICS.TID AND EMAIL2 = ? " +
                            "UNION SELECT EMAIL2 FROM FRIENDS, USERTOPICS, TOPICWORDS " +
                            "WHERE EMAIL2 = USERTOPICS.EMAIL AND TOPICWORDS.WORD = ? AND TOPICWORDS.TID = USERTOPICS.TID AND EMAIL1 = ?";
                    q.pQuery(sql);
                    q.getPstmt().setString(1, this.email);
                    q.getPstmt().setString(2, this.topics);
                    q.getPstmt().setString(3, this.user);
                    q.getPstmt().setString(4, this.topics);
                    q.getPstmt().setString(5, this.user);
//                    q.getPstmt().setString(6, this.email);
//                    q.getPstmt().setString(7, this.user);

                }
                rs = q.getPstmt().executeQuery();
                while (rs.next()) {
                    String email = rs.getString("email");
                    result.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                q.close();
            }
        }


        return result;
    }

    @Override
    protected ObservableList<String> call() throws Exception {
        return fetchUsers();
    }
}
