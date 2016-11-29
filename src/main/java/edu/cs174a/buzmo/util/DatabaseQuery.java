package edu.cs174a.buzmo.util;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class DatabaseQuery {
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public DatabaseQuery() throws SQLException, ClassNotFoundException {
        this.conn = Database.getInstance().getConnection();
        this.stmt = null;
        this.pstmt = null;
        this.rs = null;
    }

    public ResultSet query(String sql) throws SQLException, ClassNotFoundException {
        System.out.println("Creating statement...");
        stmt = conn.createStatement();

        rs = stmt.executeQuery(sql);

        return rs;
    }

    public void pQuery(String sql) throws SQLException {
        System.out.println("Creating prepared statement...");
        pstmt = conn.prepareStatement(sql);
    }

    public PreparedStatement getPstmt() {
        return pstmt;
    }

    public void close()  {
        try {
            DbUtils.close(rs);
            DbUtils.close(stmt);
            DbUtils.close(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
