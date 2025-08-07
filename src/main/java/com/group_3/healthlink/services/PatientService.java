package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;

import java.sql.*;

public class PatientService {
  /**
   * Get a patient by their userId.
   *
   * @param userId the ID of the user
   * @return the Patient object if found, null otherwise
   */
  public static Patient getByUserId(int userId) {
    Patient patient = null;
    String query = "SELECT * FROM patient WHERE user_id = ?";

    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, userId);
      java.sql.ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        patient = getPatient(rs);

        rs.close();
        stmt.close();
        return patient;
      }

      rs.close();
      stmt.close();
      return null;
    } catch (Exception e) {
      System.err.println("Error getPatientById: " + e.getMessage());
      return null;
    }
  }

  /**
   * Get a patient by their patientId.
   *
   * @param patientId the ID of the patient
   * @return the Patient object if found, null otherwise
   */
  public static Patient getByPatientId(int patientId) {
    Patient patient = null;
    String query = "SELECT * FROM patient WHERE patient_id = ?";

    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      java.sql.ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        patient = getPatient(rs);

        rs.close();
        stmt.close();
        return patient;
      }

      rs.close();
      stmt.close();
      return null;
    } catch (Exception e) {
      System.err.println("Error getPatientById: " + e.getMessage());
      return null;
    }
  }

  /**
   * Get a list of Doctors assigned to a patient.
   *
   * @param patientId the ID of the patient
   * @return an array of Doctor objects assigned to the patient
   */
  public static Doctor[] getDoctors(int patientId) {
    java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

    String query = "SELECT * FROM assigned_to WHERE patient_id = ?";
    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setInt(1, patientId);
      ResultSet rs = stmt.executeQuery();

      java.util.List<Doctor> doctors = new java.util.ArrayList<>();
      while (rs.next()) {
        int doctorId = rs.getInt("doctor_id");
        Doctor doctor = DoctorService.getByDoctorId(doctorId);
        if (doctor != null) {
          User user = AuthService.getUserById(doctor.getUserId());
          if (user != null) {
            doctor.setFirstName(user.getFirstName());
            doctor.setLastName(user.getLastName());
          }
          doctors.add(doctor);
        }
      }

      rs.close();
      stmt.close();
      return doctors.toArray(new Doctor[0]);
    } catch (Exception e) {
      System.err.println("Error getDoctors" + e.getMessage());
      return new Doctor[0];
    }
  }

  /**
   * Creates a new Patient in the database.
   *
   * @param userId                      the ID of the user
   * @param dateOfBirth                 the date of birth of the patient
   * @param phoneNumber                 the phone number of the patient
   * @param address                     the address of the patient
   * @param emergencyContactName        the name of the emergency contact
   * @param emergencyContactPhoneNumber the phone number of the emergency contact
   * @return true if the patient was created successfully, false otherwise
   */
  public static boolean createNew(int userId, String dateOfBirth, String phoneNumber, String address,
      String emergencyContactName, String emergencyContactPhoneNumber) {
    Connection con = DatabaseMgr.getInstance().getConnection();
    String query = "INSERT INTO patient (date_of_birth, phone_number, address, emergency_contact_name, " +
        "emergency_contact_phone_number, user_id) VALUES (?, ?, ?, ?, ?, ?)";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, dateOfBirth);
      stmt.setString(2, phoneNumber);
      stmt.setString(3, address);
      stmt.setString(4, emergencyContactName);
      stmt.setString(5, emergencyContactPhoneNumber);
      stmt.setInt(6, userId);

      int rowsAffected = stmt.executeUpdate();
      stmt.close();

      return rowsAffected > 0;
    } catch (SQLException e) {
      System.err.println("Error creating new patient: " + e.getMessage());
      return false;
    }
  }

  /**
   * Updates an existing Patient's information in the database.
   *
   * @param patientId                   the ID of the patient to update
   * @param dateOfBirth                 the new date of birth of the patient
   * @param phoneNumber                 the new phone number of the patient
   * @param address                     the new address of the patient
   * @param emergencyContactName        the new name of the emergency contact
   * @param emergencyContactPhoneNumber the new phone number of the emergency
   *                                    contact
   * @return true if the patient was updated successfully, false otherwise
   */
  public static boolean updatePatient(int patientId,
      String dateOfBirth, String phoneNumber, String address,
      String emergencyContactName, String emergencyContactPhoneNumber) {
    Connection con = DatabaseMgr.getInstance().getConnection();
    String query = "UPDATE patient SET date_of_birth = ?, phone_number = ?, address = ?, " +
        "emergency_contact_name = ?, emergency_contact_phone_number = ? WHERE patient_id = ?";

    try {
      PreparedStatement stmt = con.prepareStatement(query);
      stmt.setString(1, dateOfBirth);
      stmt.setString(2, phoneNumber);
      stmt.setString(3, address);
      stmt.setString(4, emergencyContactName);
      stmt.setString(5, emergencyContactPhoneNumber);
      stmt.setInt(6, patientId);

      int rowsAffected = stmt.executeUpdate();
      stmt.close();

      return rowsAffected > 0;
    } catch (SQLException e) {
      System.err.println("Error updatePatient: " + e.getMessage());
      return false;
    }
  }

  /**
   * Retrieves a Patient object from the ResultSet.
   *
   * @param rs the ResultSet containing patient data
   * @return a Patient object populated with data from the ResultSet
   * @throws SQLException if an error occurs while accessing the ResultSet
   **/
  private static Patient getPatient(ResultSet rs) throws SQLException {
    Patient patient;
    patient = new Patient();
    patient.setPatientId(rs.getInt("patient_id"));
    patient.setDateOfBirth(rs.getString("date_of_birth"));
    patient.setPhoneNumber(rs.getString("phone_number"));
    patient.setAddress(rs.getString("address"));
    patient.setEmergencyContactName(rs.getString("emergency_contact_name"));
    patient.setEmergencyContactPhoneNumber(rs.getString("emergency_contact_phone_number"));
    patient.setUserId(rs.getInt("user_id"));

    return patient;
  }
}
