package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.SystemLog;
import com.group_3.healthlink.SystemLogAction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import java.util.List;
import java.util.ArrayList;

public class SystemLogService {
  /**
   * Create a new system log entry.
   * @param userId the ID of the user who performed the action
   * @param action the action performed by the user
   * @param detail additional details about the action
   * @return true if the log entry was created successfully, false otherwise
   */
  public static boolean createNew(int userId, SystemLogAction action, String detail) {
    Connection con = DatabaseMgr.getInstance().getConnection();

    String query = "INSERT INTO system_log (user_id, action, detail, timestamp) VALUES (?, ?, ?, ?)";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, userId);
      stmt.setInt(2, action.getCode());
      stmt.setString(3, detail);
      stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

      int rowsAffected = stmt.executeUpdate();
      stmt.close();

      if (rowsAffected > 0) {
        System.out.println("Successfully created system log entry for userId: " + userId);
      } else {
        System.out.println("Failed to create system log entry for userId: " + userId);
      }

      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error creating system log entry: " + e.getMessage());
      return false;
    }
  }

  public static SystemLog getById(int systemLogId) {
    Connection con = DatabaseMgr.getInstance().getConnection();
    String query = "SELECT * FROM system_log WHERE log_id = ?";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, systemLogId);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        SystemLog log = getSystemLog(rs);

        rs.close();
        stmt.close();
        return log;
      } else {
        rs.close();
        stmt.close();
        return null;
      }
    } catch (Exception e) {
      System.err.println("Error getSystemLog: " + e.getMessage());
      return null;
    }
  }

  public static List<SystemLog> getAll() {
    Connection con = DatabaseMgr.getInstance().getConnection();
    String query = "SELECT * FROM system_log ORDER BY timestamp DESC";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();

      List<SystemLog> logs = new ArrayList<>();
      while (rs.next()) {
        SystemLog log = getSystemLog(rs);
        logs.add(log);
      }

      rs.close();
      stmt.close();
      return logs;
    } catch (Exception e) {
      System.err.println("Error getAllSystemLogs: " + e.getMessage());
      return null;
    }
  }

  private static SystemLog getSystemLog(ResultSet rs) throws SQLException {
    SystemLog log = new SystemLog();
    log.setLogId(rs.getInt("log_id"));
    log.setUserId(rs.getInt("user_id"));
    log.setAction(
      SystemLogAction.fromCode(rs.getInt("action"))
    );
    log.setDetail(rs.getString("detail"));
    log.setTimestamp(rs.getTimestamp("timestamp"));
    return log;
  }

  public static List<SystemLog> getByUserId(int userId) {
    Connection con = DatabaseMgr.getInstance().getConnection();
    String query = "SELECT * FROM system_log WHERE user_id = ? ORDER BY timestamp DESC";

    try {
      java.sql.PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, userId);
      java.sql.ResultSet rs = stmt.executeQuery();

      java.util.List<SystemLog> logs = new java.util.ArrayList<>();
      while (rs.next()) {
        SystemLog log = getSystemLog(rs);
        logs.add(log);
      }

      rs.close();
      stmt.close();
      return logs;
    } catch (Exception e) {
      System.err.println("Error getSystemLogsByUserId: " + e.getMessage());
      return null;
    }
  }
}
