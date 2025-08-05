package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Notification;
import com.group_3.healthlink.User;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    
    /**
     * Create a new notification
     */
    public static boolean createNotification(int senderId, int receiverId, String message, String type) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "INSERT INTO notification (sender_id, receiver_id, message, type) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, message);
            stmt.setString(4, type);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all notifications for a user (receiver)
     */
    public static List<Notification> getNotificationsByReceiverId(int receiverId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        List<Notification> notifications = new ArrayList<>();
        
        try {
            String query = "SELECT n.*, u.first_name, u.last_name " +
                          "FROM notification n " +
                          "LEFT JOIN user u ON n.sender_id = u.user_id " +
                          "WHERE n.receiver_id = ? " +
                          "ORDER BY n.timestamp DESC";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, receiverId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Notification notification = getNotificationFromResultSet(rs);
                notifications.add(notification);
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("Error getting notifications: " + e.getMessage());
        }
        
        return notifications;
    }
    
    /**
     * Get unread notifications count for a user
     */
    public static int getUnreadNotificationCount(int receiverId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT COUNT(*) FROM notification WHERE receiver_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, receiverId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("Error getting unread notification count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Mark a notification as read
     */
    public static boolean markAsRead(int notificationId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "UPDATE notification SET is_read = 1 WHERE notification_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, notificationId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mark all notifications as read for a user
     */
    public static boolean markAllAsRead(int receiverId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "UPDATE notification SET is_read = 1 WHERE receiver_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, receiverId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a notification
     */
    public static boolean deleteNotification(int notificationId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "DELETE FROM notification WHERE notification_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, notificationId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a single notification by ID
     */
    public static Notification getNotificationById(int notificationId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT n.*, u.first_name, u.last_name " +
                          "FROM notification n " +
                          "LEFT JOIN user u ON n.sender_id = u.user_id " +
                          "WHERE n.notification_id = ?";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, notificationId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Notification notification = getNotificationFromResultSet(rs);
                rs.close();
                stmt.close();
                return notification;
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("Error getting notification by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Helper method to create Notification object from ResultSet
     */
    private static Notification getNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setSenderId(rs.getInt("sender_id"));
        notification.setReceiverId(rs.getInt("receiver_id"));
        notification.setMessage(rs.getString("message"));
        notification.setTimestamp(rs.getTimestamp("timestamp"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setType(rs.getString("type"));
        
        // Set sender name if available
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        if (firstName != null && lastName != null) {
            notification.setSenderName(firstName + " " + lastName);
        } else if (firstName != null) {
            notification.setSenderName(firstName);
        }
        
        return notification;
    }
    
    /**
     * Create notification for doctor assignment
     */
    public static boolean createDoctorAssignmentNotification(int patientId, int doctorId, boolean isAssignment) {
        try {
            User patient = AuthService.getUserById(patientId);
            User doctor = AuthService.getUserById(doctorId);
            
            if (patient == null || doctor == null) {
                return false;
            }
            
            String message;
            if (isAssignment) {
                message = "Patient " + patient.getFullName() + " has added you as their doctor.";
            } else {
                message = "Patient " + patient.getFullName() + " has removed you as their doctor.";
            }
            
            return createNotification(patientId, doctorId, message, "doctor_assignment");
            
        } catch (Exception e) {
            System.err.println("Error creating doctor assignment notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create notification for test result upload
     */
    public static boolean createTestResultNotification(int patientId, int doctorId, String fileName) {
        try {
            // Get patient and doctor user IDs
            Patient patient = PatientService.getByPatientId(patientId);
            Doctor doctor = DoctorService.getByDoctorId(doctorId);
            
            if (patient == null || doctor == null) {
                return false;
            }
            
            User patientUser = AuthService.getUserById(patient.getUserId());
            User doctorUser = AuthService.getUserById(doctor.getUserId());
            
            if (patientUser == null || doctorUser == null) {
                return false;
            }
            
            String message = patientUser.getFullName() + " uploaded [" + fileName + "] to their test results.";
            
            return createNotification(patientUser.getUserId(), doctorUser.getUserId(), message, "test_result");
            
        } catch (Exception e) {
            System.err.println("Error creating test result notification: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create notification for medication prescription
     */
    public static boolean createMedicationNotification(int doctorId, int patientId, String medicationName) {
        try {
            // Get patient and doctor user IDs
            Patient patient = PatientService.getByPatientId(patientId);
            Doctor doctor = DoctorService.getByDoctorId(doctorId);
            
            if (patient == null || doctor == null) {
                return false;
            }
            
            User patientUser = AuthService.getUserById(patient.getUserId());
            User doctorUser = AuthService.getUserById(doctor.getUserId());
            
            if (patientUser == null || doctorUser == null) {
                return false;
            }
            
            String message = "Doctor " + doctorUser.getFullName() + " has prescribed a new medication for you: " + medicationName;
            
            return createNotification(doctorUser.getUserId(), patientUser.getUserId(), message, "medication");
            
        } catch (Exception e) {
            System.err.println("Error creating medication notification: " + e.getMessage());
            return false;
        }
    }
} 