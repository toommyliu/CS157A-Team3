package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AssignmentService {
    /**
     * Assigns a patient to a doctor.
     *
     * @param patientId the id of the patient to assign
     * @param doctorId  the id of the doctor to assign the patient to
     * @return true if the assignment was successful, false otherwise
     */
    public static boolean assignPatientToDoctor(int patientId, int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "INSERT INTO assigned_to (patient_id, doctor_id) VALUES (?, ?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            if (rowsAffected > 0) {
                Patient patient = PatientService.getByPatientId(patientId);
                Doctor doctor = DoctorService.getByDoctorId(doctorId);

                // Create notificatino for doctor assignment
                if (patient != null && doctor != null) {
                    NotificationService.createDoctorAssignmentNotification(patient.getUserId(), doctor.getUserId(),
                            true);
                }

                System.out.println("Successfully assigned patient " + patientId + " to doctor " + doctorId);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning patient to doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes an assignment of a patient to a doctor.
     *
     * @param patientId the id of the patient to remove
     * @param doctorId  the id of the doctor to remove the patient from
     * @return true if the removal was successful, false otherwise
     */
    public static boolean removeAssignment(int patientId, int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "DELETE FROM assigned_to WHERE patient_id = ? AND doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            boolean success = rowsAffected > 0;

            if (success) {
                Patient patient = PatientService.getByPatientId(patientId);
                Doctor doctor = DoctorService.getByDoctorId(doctorId);

                // Create notification for doctor assignment removal
                if (patient != null && doctor != null) {
                    NotificationService.createDoctorAssignmentNotification(patient.getUserId(), doctor.getUserId(),
                            false);
                }
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error removing assignment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a patient is assigned to a doctor.
     *
     * @param patientId the id of the patient to check
     * @param doctorId  the id of the doctor to check
     * @return true if the patient is assigned to the doctor, false otherwise
     */
    public static boolean isAssigned(int patientId, int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT COUNT(*) FROM assigned_to WHERE patient_id = ? AND doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);

            java.sql.ResultSet rs = stmt.executeQuery();
            boolean assigned = false;

            if (rs.next()) {
                assigned = rs.getInt(1) > 0;
            }

            rs.close();
            stmt.close();
            return assigned;
        } catch (SQLException e) {
            System.err.println("Error checking assignment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a list of doctor IDs assigned to a patient.
     *
     * @param patientId the id of the patient to get assigned doctor IDs for
     * @return a list of doctor IDs assigned to the patient
     */
    public static List<Integer> getAssignedDoctorIds(int patientId) {
        List<Integer> doctorIds = new ArrayList<>();
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT doctor_id FROM assigned_to WHERE patient_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctorIds.add(rs.getInt("doctor_id"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting assigned doctor IDs: " + e.getMessage());
        }

        return doctorIds;
    }
}