package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;

import java.sql.*;

public class NotesService {
  public static boolean createNote(String content, int patientId, int doctorId) {
    String query = "INSERT INTO note (content, patient_id, doctor_id, created_at) VALUES (?, ?, ?, ?)";
    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, content);
      stmt.setInt(2, patientId);

      if (doctorId != -1) {
        stmt.setInt(3, doctorId);
      } else {
        stmt.setNull(3, Types.INTEGER);
      }

      stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      con.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error createNote: " + e.getMessage());
      return false;
    }
  }
}

