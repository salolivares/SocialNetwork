package edu.cs174a.buzmo.tasks;

import edu.cs174a.buzmo.util.DatabaseQuery;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class DeletePrivateMessageTask extends Task<Void> {
    private String email;
    private String sender;
    private int mid;

    public DeletePrivateMessageTask(String email, String sender, int mid) {
        this.email = email; this.sender = sender; this.mid = mid;

        System.out.println("Email is: " + email + " Sender is: " + sender);
    }

    private void deleteMessage() {
        DatabaseQuery q = null;
        try {
            q = new DatabaseQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int flag = 0;

        try {
            String sql = "SELECT FLAG FROM PRIVATEMESSAGES WHERE MID = ?";
            q.pQuery(sql);
            q.getPstmt().setInt(1, this.mid);
            ResultSet rs = q.getPstmt().executeQuery();
            while (rs.next()) {
                flag = rs.getInt("flag");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("flag is: " + flag);

        //if sender wants to remove their message, only rec or none can see
        if (this.email.equals(this.sender)) {
            try {
                String sql = "";
                if (flag == 0) {
                    System.out.println("in flag = 0");
                    sql = "UPDATE PRIVATEMESSAGES SET FLAG = 1 WHERE MID = ? AND FLAG = 0";
                }
                if (flag == 2) {
                    System.out.println("in flag = 2");
                    sql = "UPDATE PRIVATEMESSAGES SET FLAG = 3 WHERE MID = ? AND FLAG = 2";
                }
                q.pQuery(sql);
                q.getPstmt().setInt(1, this.mid);
                q.getPstmt().executeUpdate();
                q.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //if user wants to remove a message he received, only sender can see or none can see
        else {
            try {
                String sql = "";
                if (flag == 0) {
                    System.out.println("in flag = 0");
                    sql = "UPDATE PRIVATEMESSAGES SET FLAG = 2 WHERE MID = ? AND FLAG = 0";
                }
                if (flag == 1) {
                    System.out.println("in flag = 1");
                    sql = "UPDATE PRIVATEMESSAGES SET FLAG = 3 WHERE MID = ? AND FLAG = 1";
                }
                q.pQuery(sql);
                q.getPstmt().setInt(1, this.mid);
                q.getPstmt().executeUpdate();
                q.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Void call() throws Exception {
        deleteMessage();
        return null;
    }
}
