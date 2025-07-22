package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.services.AuthService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorService {
    public static Doctor getByUserId(int userId) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT * FROM doctor WHERE user_id = ?";

        try {
            java.sql.PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return getDoctor(stmt, rs);
            }

            rs.close();
            stmt.close();

            return null;
        } catch (Exception e) {
            System.err.println("Error getByUserId: " + e.getMessage());
            return null;
        }
    }

    private static Doctor getDoctor(PreparedStatement stmt, ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setDepartment(rs.getString("department"));
        doctor.setUserId(rs.getInt("user_id"));

        rs.close();
        stmt.close();

        return doctor;
    }

    public static Doctor getByDoctorId(int doctorId) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT * FROM doctor WHERE doctor_id = ?";

        try {
            java.sql.PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setUserId(rs.getInt("user_id"));

                rs.close();
                stmt.close();

                User user = AuthService.getUserById(doctor.getUserId());
                if (user != null) {
                    doctor.setFirstName(user.getFirstName());
                    doctor.setLastName(user.getLastName());
                    doctor.setEmailAddress(user.getEmailAddress());
                    doctor.setRole(user.getRole());
                    doctor.setCreatedAt(user.getCreatedAt());
                    doctor.setUpdatedAt(user.getUpdatedAt());
                }

                return doctor;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (Exception e) {
            System.err.println("Error getByDoctorId: " + e.getMessage());
            return null;
        }
    }

    public static Patient[] getPatients(int doctorId) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT * FROM assigned_to WHERE doctor_id = ?";

        try {
            java.sql.PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            java.sql.ResultSet rs = stmt.executeQuery();

            java.util.List<Patient> patients = new java.util.ArrayList<>();
            while (rs.next()) {
                int patientId = rs.getInt("patient_id");
                Patient patient = PatientService.getByPatientId(patientId);
                if (patient != null) {
                    User user = AuthService.getUserById(patient.getUserId());
                    if (user != null) {
                        patient.setUser(user);
                    }
                    patients.add(patient);
                }
            }

            rs.close();
            stmt.close();
            return patients.toArray(new Patient[0]);
        } catch (Exception e) {
            System.err.println("Error getPatients" + e.getMessage());
            return new Patient[0];
        }
    }

}
