package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.SQLException;

public class UpdateDBTimeTask extends Task<Void> {
    private String date;
    private String time;

    public UpdateDBTimeTask(String date, String time) {
        this.date = date;
        this.time = time;
    }

    @Override
    protected Void call() throws Exception {
        updateTime();
        return null;
    }

    private void updateTime() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            String sql = "UPDATE SYSTEMDATETIME SET date1 = ?, time = ? WHERE DID = 1";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.date);
            q.getPstmt().setString(2, this.time);
            q.getPstmt().executeUpdate();
            q.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
