package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;

import java.sql.Connection;

public class SystemLogService {
  public static boolean createNew(int userId, String action, String detail) {
    Connection con = DatabaseMgr.getInstance().getConnection();

    String query = "INSERT INTO system_log (user_id, action, detail, timestamp) VALUES (?, ?, ?, ?)";

    try {
      java.sql.PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, userId);
      stmt.setString(2, action);
      stmt.setString(3, detail);
      stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

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
}
