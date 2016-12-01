package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchDBTask extends Task<String[]> {
    @Override
    protected String[] call() throws Exception {
        return fetchDataAndTime();
    }

    private String[] fetchDataAndTime(){
        DatabaseQuery q = null;
        String[] result = new String[2];

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        String sql = "SELECT * FROM SYSTEMDATETIME WHERE DID = 1";
        ResultSet rs = null;

        try {
            rs = q.query(sql);
            while(rs.next()){
                result[0] = rs.getString("date1");
                result[1] = rs.getString("time");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }


        return result;
    }
}
