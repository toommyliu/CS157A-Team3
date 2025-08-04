package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AssignmentService {
    public static boolean assignPatientToDoctor(int patientId, int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "INSERT INTO assigned_to (patient_id, doctor_id) VALUES (?, ?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            SystemLogService.createNew(patientId, "Assigned Doctor", "Assigned doctorId " + doctorId);

            System.out.println("Successfully assigned patient " + patientId + " to doctor " + doctorId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning patient to doctor: " + e.getMessage());
            return false;
        }
    }

    public static boolean removeAssignment(int patientId, int doctorId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "DELETE FROM assigned_to WHERE patient_id = ? AND doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            SystemLogService.createNew(patientId, "Removed Doctor Assignment", "Unassigned doctorId " + doctorId);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing assignment: " + e.getMessage());
            return false;
        }
    }

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