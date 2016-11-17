package edu.cs174a.buzmo.util;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class DatabaseQuery {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public DatabaseQuery() throws SQLException, ClassNotFoundException {
        this.conn = Database.getInstance().getConnection();
        this.stmt = null;
        this.rs = null;
    }

    public ResultSet query(String sql) throws SQLException, ClassNotFoundException {
        System.out.println("Creating statement...");
        stmt = conn.createStatement();

        rs = stmt.executeQuery(sql);

        return rs;
    }

    public void close() throws SQLException {
        DbUtils.close(rs);
        DbUtils.close(stmt);
    }




}
