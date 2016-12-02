package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import edu.cs174a.buzmo.util.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class FetchNumMessageReads extends Task<Integer> {
    private String groupName;
    private int duration;
    private LocalDate globalDate;

    public FetchNumMessageReads(String groupName, int duration, LocalDate globalDate) {
        this.groupName = groupName;
        this.duration = duration;
        this.globalDate = globalDate;
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
            String sql = "SELECT num_read, timestamp FROM MESSAGES";
            q.pQuery(sql);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                // DO SOME TIMESTAMP CALCULATIONS
                LocalDate date = LocalDate.parse(rs.getDate("timestamp").toString());

                int daysBetween = (int) DAYS.between(date, globalDate);

                System.out.println(daysBetween);

                if(daysBetween <= duration){
                    result += rs.getInt("num_read");
                }

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
        return fetchMessages();
    }
}
