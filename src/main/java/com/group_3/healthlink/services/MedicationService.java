package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import java.sql.*;

public class MedicationService {
  public static boolean createMedication(
    int patientId, int doctorId, 
    String medicationName, String dosage, String frequency, String noteContent
  ) {
    String query = "INSERT INTO medication (patient_id, doctor_id, name, dosage, frequency, notes) VALUES (?, ?, ?, ?, ?, ?)";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      stmt.setInt(2, doctorId);
      stmt.setString(3, medicationName);
      stmt.setString(4, dosage);
      stmt.setString(5, frequency);
      if (noteContent != null && !noteContent.trim().isEmpty()) {
        stmt.setString(6, noteContent);
      } else {
        stmt.setNull(6, Types.VARCHAR);
      }

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error createMedication: " + e.getMessage());
      return false;
    }
  }
}
