package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicationLogService {
  public static boolean createEntry(int medicationId, int patientId, String dosageTaken, String note, java.sql.Timestamp takenAt) {
    Connection con = DatabaseMgr.getInstance().getConnection();

    String query = "INSERT INTO medication_log (medication_id, patient_id, taken_at, dosage_taken, created_at, note) VALUES (?, ?, ?, ?, ?, ?)";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, medicationId);
      stmt.setInt(2, patientId);
      stmt.setTimestamp(3, takenAt);
      stmt.setString(4, dosageTaken);
      stmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
      if (note == null || note.trim().isEmpty()) {
        stmt.setNull(6, java.sql.Types.VARCHAR);
      } else {
        stmt.setString(6, note);
      }

      int rowsAffected = stmt.executeUpdate();
      stmt.close();

      if (rowsAffected > 0) {
        System.out.println("New medication log created successfully.");
        return true;
      }

      return false;
    } catch (java.sql.SQLException e) {
      System.out.println("Error creating new medication log: " + e.getMessage());
      return false;
    }
  }
}
