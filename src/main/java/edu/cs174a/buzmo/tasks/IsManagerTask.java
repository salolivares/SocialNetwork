package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IsManagerTask extends Task<Integer> {
    private String email;

    public IsManagerTask(String email) {
        this.email = email;
    }

    private Integer isManager(){
        DatabaseQuery q = null;
        Integer result = null;

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        ResultSet rs = null;

        try {

            String sql = "SELECT is_manager FROM USERS WHERE email = ?";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.email);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                result = rs.getInt("is_manager");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }

        return result;
    }

    @Override
    protected Integer call() throws Exception {
        return isManager();
    }
}
