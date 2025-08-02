package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Medication;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicationService {
  public static boolean createMedication(
      int patientId, int doctorId,
      String medicationName, String dosage, String frequency, String noteContent) {
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

  public static List<Medication> getMedicationsByPatientId(int patientId) {
    List<Medication> medications = new ArrayList<>();
    String query = "SELECT * FROM medication WHERE patient_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();
    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Medication med = new Medication();
        med.setId(rs.getInt("medication_id"));
        med.setPatientId(rs.getInt("patient_id"));
        med.setDoctorId(rs.getInt("doctor_id"));
        med.setName(rs.getString("name"));
        med.setDosage(rs.getString("dosage"));
        med.setFrequency(rs.getString("frequency"));
        med.setNotes(rs.getString("notes"));
        medications.add(med);
      }

      rs.close();
      stmt.close();
    } catch (Exception e) {
      System.err.println("Error getMedicationsByPatientId: " + e.getMessage());
    }

    return medications;
  }

  public static Medication getMedicationById(int medicationId) {
    String query = "SELECT * FROM medication WHERE medication_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();
    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, medicationId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Medication med = new Medication();
        med.setId(rs.getInt("medication_id"));
        med.setPatientId(rs.getInt("patient_id"));
        med.setDoctorId(rs.getInt("doctor_id"));
        med.setName(rs.getString("name"));
        med.setDosage(rs.getString("dosage"));
        med.setFrequency(rs.getString("frequency"));
        med.setNotes(rs.getString("notes"));
        rs.close();
        stmt.close();
        return med;
      }

      rs.close();
      stmt.close();
      return null;
    } catch (Exception e) {
      System.err.println("Error getMedicationById: " + e.getMessage());
      return null;
    }
  }

  public static boolean updateMedication(int medicationId, String medicationName, String dosage, String frequency,
      String noteContent) {
    String query = "UPDATE medication SET name = ?, dosage = ?, frequency = ?, notes = ? WHERE medication_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, medicationName);
      stmt.setString(2, dosage);
      stmt.setString(3, frequency);

      if (noteContent != null && !noteContent.trim().isEmpty()) {
        stmt.setString(4, noteContent);
      } else {
        stmt.setNull(4, Types.VARCHAR);
      }

      stmt.setInt(5, medicationId);

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error updateMedication: " + e.getMessage());
      return false;
    }
  }

  public static boolean deleteMedication(int medicationId) {
    String query = "DELETE FROM medication WHERE medication_id = ?";
    Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, medicationId);

      int rowsAffected = stmt.executeUpdate();
      stmt.close();
      return rowsAffected > 0;
    } catch (Exception e) {
      System.err.println("Error deleteMedication: " + e.getMessage());
      return false;
    }
  }

  public static HashMap<Integer, Medication> getMedicationsByPatientIdMap(int patientId) {
    List<Medication> medications = getMedicationsByPatientId(patientId);
    HashMap<Integer, Medication> medicationMap = new HashMap<Integer, Medication>();
    for (Medication med : medications) {
      medicationMap.put(med.getId(), med);
    }
    return medicationMap;
  }
}
