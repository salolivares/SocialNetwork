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
        this.numMessages = Integer.parseInt(numMessages);
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

        String sql = "SELECT EMAIL FROM USERS WHERE USERS.EMAIL = " + "'" + this.email + "'";
        ResultSet rs = null;

        try {
            rs = q.query(sql);
            while(rs.next()){
                String email = rs.getString("email");
                result.add(email);
            }
        } catch (SQLException | ClassNotFoundException e) {
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
