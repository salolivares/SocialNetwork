package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class FetchTotalNumOfNewMessagesTask extends Task<Integer> {
    private LocalDate globalDate;

    public FetchTotalNumOfNewMessagesTask(LocalDate globalDate) {
        this.globalDate = globalDate;
    }

    @Override
    protected Integer call() throws Exception {
        return fetchMessages();
    }

    private Integer fetchMessages(){
        DatabaseQuery q = null;
        Integer result = 0;

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        ResultSet rs = null;

        try {
            String sql = "SELECT timestamp from MESSAGES";
            q.pQuery(sql);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                // DO SOME TIMESTAMP CALCULATIONS
                LocalDate date = LocalDate.parse(rs.getDate("timestamp").toString());

                int daysBetween = (int) DAYS.between(date, globalDate);

                System.out.println(daysBetween);

                if(daysBetween <= 7){
                    result++;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            q.close();
        }


        return result;
    }
}
