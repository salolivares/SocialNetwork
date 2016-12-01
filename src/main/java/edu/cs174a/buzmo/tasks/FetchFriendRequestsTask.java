package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class FetchFriendRequestsTask extends Task<ObservableList<String>> {
    private String email;

    public FetchFriendRequestsTask(String email) {
        this.email = email;
    }

    private ObservableList<String> fetchRequests()  {
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

            String sql = "SELECT EMAIL1 FROM FRIENDS WHERE FRIENDS.EMAIL2 = ? AND FRIENDS.REQUEST_STATUS = 0";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                String email = rs.getString("email1");
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
        return fetchRequests();
    }
}
