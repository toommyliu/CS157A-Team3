package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;

import java.sql.*;

public class PatientService {
    // Get a Patient by foreign key userId
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

    // Get a patient by primary key patientId
    public static Patient getByPatientId(int patientId) {
        Patient patient = null;
        String query = "SELECT * FROM patient WHERE patient_id = ?";

        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("got rs.next");
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

    // Get doctors assigned to a patient
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
