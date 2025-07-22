package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    public static Doctor getByUserId(int userId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT d.doctor_id, d.department, d.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM doctor d JOIN user u ON d.user_id = u.user_id WHERE d.user_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setUserId(rs.getInt("user_id"));
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));
                doctor.setEmailAddress(rs.getString("email_address"));
                doctor.setRole(UserRole.Doctor);
                doctor.setCreatedAt(rs.getTimestamp("created_at"));
                doctor.setUpdatedAt(rs.getTimestamp("updated_at"));

                rs.close();
                stmt.close();
                return doctor;
            }

            rs.close();
            stmt.close();
            return null;
        } catch (Exception e) {
            System.err.println("Error getByUserId: " + e.getMessage());
            return null;
        }
    }

    public static Doctor getByDoctorId(int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT d.doctor_id, d.department, d.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM doctor d JOIN user u ON d.user_id = u.user_id WHERE d.doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setDepartment(rs.getString("department"));
                doctor.setUserId(rs.getInt("user_id"));
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));
                doctor.setEmailAddress(rs.getString("email_address"));
                doctor.setRole(UserRole.valueOf(rs.getString("role")));
                doctor.setCreatedAt(rs.getTimestamp("created_at"));
                doctor.setUpdatedAt(rs.getTimestamp("updated_at"));

                rs.close();
                stmt.close();
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
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT p.patient_id, p.date_of_birth, p.phone_number, p.address, " +
                "p.emergency_contact_name, p.emergency_contact_phone_number, p.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM assigned_to a " +
                "JOIN patient p ON a.patient_id = p.patient_id " +
                "JOIN user u ON p.user_id = u.user_id " +
                "WHERE a.doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            List<Patient> patients = new ArrayList<>();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("patient_id"));
                patient.setDateOfBirth(rs.getString("date_of_birth"));
                patient.setPhoneNumber(rs.getString("phone_number"));
                patient.setAddress(rs.getString("address"));
                patient.setEmergencyContactName(rs.getString("emergency_contact_name"));
                patient.setEmergencyContactPhoneNumber(rs.getString("emergency_contact_phone_number"));
                patient.setUserId(rs.getInt("user_id"));
                patient.setFirstName(rs.getString("first_name"));
                patient.setLastName(rs.getString("last_name"));
                patient.setEmailAddress(rs.getString("email_address"));
                patient.setRole(UserRole.valueOf(rs.getString("role")));
                patient.setCreatedAt(rs.getTimestamp("created_at"));
                patient.setUpdatedAt(rs.getTimestamp("updated_at"));
                patients.add(patient);
            }

            rs.close();
            stmt.close();
            return patients.toArray(new Patient[0]);
        } catch (Exception e) {
            System.err.println("Error getPatients: " + e.getMessage());
            return new Patient[0];
        }
    }

}
