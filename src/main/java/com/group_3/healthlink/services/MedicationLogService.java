package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.MedicationLog;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

  public static MedicationLog[] getMedicationLogsByPatientId(int patientId) {
    Connection con = DatabaseMgr.getInstance().getConnection();

    String query = "SELECT * FROM medication_log WHERE patient_id = ?";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      java.sql.ResultSet rs = stmt.executeQuery();

      java.util.List<MedicationLog> logs = new java.util.ArrayList<>();
      while (rs.next()) {
        MedicationLog log = new MedicationLog();
        log.setMedicationLogId(rs.getInt("medication_log_id"));
        log.setMedicationId(rs.getInt("medication_id"));
        log.setPatientId(rs.getInt("patient_id"));
        log.setTakenAt(rs.getTimestamp("taken_at"));
        log.setDosageTaken(rs.getString("dosage_taken"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        log.setNote(rs.getString("note"));
        logs.add(log);
      }

      rs.close();
      stmt.close();
      return logs.toArray(new MedicationLog[0]);
    } catch (java.sql.SQLException e) {
      System.out.println("Error get medication logs by patient id " + e.getMessage());
      return new MedicationLog[0];
    }
  }
}
