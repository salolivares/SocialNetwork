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

public class FetchChatGroupMessageTask extends Task<ObservableList<Message>> {
    private String groupName;
    private int duration;
    private LocalDate globalDate;

    public FetchChatGroupMessageTask(String groupName, int duration, LocalDate globalDate) {
        this.groupName = groupName;
        this.duration = duration;
        this.globalDate = globalDate;
    }

    @Override
    protected ObservableList<Message> call() throws Exception {
        return fetchMessages();
    }

    private ObservableList<Message> fetchMessages(){
        DatabaseQuery q = null;
        ObservableList<Message> result = FXCollections.observableArrayList();

        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM " +
                    "(SELECT MESSAGES.* " +
                    "FROM MESSAGES, CHATGROUPMESSAGES " +
                    "WHERE MESSAGES.mid = CHATGROUPMESSAGES.mid " +
                    "AND CHATGROUPMESSAGES.chatgroupid = (SELECT chatgroupid FROM CHATGROUPS WHERE GROUP_NAME = ? ) ) " +
                    "ORDER BY timestamp ASC";
            q.pQuery(sql);
            q.getPstmt().setString(1, this.groupName);
            rs = q.getPstmt().executeQuery();
            while(rs.next()){
                // DO SOME TIMESTAMP CALCULATIONS
                LocalDate date = LocalDate.parse(rs.getDate("timestamp").toString());

                int daysBetween = (int) DAYS.between(date, globalDate);

                System.out.println(daysBetween);

                if(daysBetween <= duration){
                    result.add(new Message(rs.getInt("mid"), rs.getString("sender"), rs.getString("body"), rs.getTimestamp("timestamp").toString()));
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
