package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Message;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.User;
import com.group_3.healthlink.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    /**
     * Get chat threads for a user (doctors for patients, patients for doctors)
     */
    public static List<User> getChatThreads(int userId, UserRole userRole) {
        List<User> chatThreads = new ArrayList<>();
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query;
            if (userRole == UserRole.Patient) {
                // For patients: get their assigned doctors
                query = "SELECT DISTINCT u.user_id, u.first_name, u.last_name, u.email_address, u.role FROM user u " +
                       "JOIN doctor d ON u.user_id = d.user_id " +
                       "JOIN assigned_to a ON d.doctor_id = a.doctor_id " +
                       "JOIN patient p ON a.patient_id = p.patient_id " +
                       "WHERE p.user_id = ? " +
                       "ORDER BY u.first_name, u.last_name";
            } else {
                // For doctors: get their assigned patients
                query = "SELECT DISTINCT u.user_id, u.first_name, u.last_name, u.email_address, u.role FROM user u " +
                       "JOIN patient p ON u.user_id = p.user_id " +
                       "JOIN assigned_to a ON p.patient_id = a.patient_id " +
                       "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                       "WHERE d.user_id = ? " +
                       "ORDER BY u.first_name, u.last_name";
            }
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmailAddress(rs.getString("email_address"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                String roleStr = rs.getString("role");
                // Convert database role to enum (capitalize first letter)
                if (roleStr != null) {
                    roleStr = roleStr.substring(0, 1).toUpperCase() + roleStr.substring(1).toLowerCase();
                    user.setRole(UserRole.valueOf(roleStr));
                }
                chatThreads.add(user);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting chat threads: " + e.getMessage());
        }
        
        return chatThreads;
    }

    /**
     * Get chat history between two users
     */
    public static List<Message> getChatHistory(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT * FROM message " +
                          "WHERE (sender_id = ? AND receiver_id = ?) " +
                          "OR (sender_id = ? AND receiver_id = ?) " +
                          "ORDER BY timestamp ASC";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.setInt(3, user2Id);
            stmt.setInt(4, user1Id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting chat history: " + e.getMessage());
        }
        
        return messages;
    }

    /**
     * Send a new message
     */
    public static boolean sendMessage(int senderId, int receiverId, String content) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "INSERT INTO message (sender_id, receiver_id, content, timestamp) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, content);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error sending message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the latest message between two users (for preview)
     */
    public static Message getLatestMessage(int user1Id, int user2Id) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT * FROM message " +
                          "WHERE (sender_id = ? AND receiver_id = ?) " +
                          "OR (sender_id = ? AND receiver_id = ?) " +
                          "ORDER BY timestamp DESC LIMIT 1";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, user1Id);
            stmt.setInt(2, user2Id);
            stmt.setInt(3, user2Id);
            stmt.setInt(4, user1Id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                rs.close();
                stmt.close();
                return message;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting latest message: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Get unread message count for a user
     */
    public static int getUnreadCount(int userId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT COUNT(*) FROM message WHERE receiver_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * Get unread message count for a specific chat thread
     */
    public static int getUnreadCountForChat(int userId, int otherUserId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT COUNT(*) FROM message WHERE receiver_id = ? AND sender_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, otherUserId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting unread count for chat: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * Mark all messages from a specific sender as read for a receiver
     */
    public static boolean markMessagesAsRead(int receiverId, int senderId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "UPDATE message SET is_read = 1 WHERE receiver_id = ? AND sender_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, receiverId);
            stmt.setInt(2, senderId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected >= 0; // Return true even if no rows were updated
        } catch (SQLException e) {
            System.err.println("Error marking messages as read: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a user has unread messages from a specific sender
     */
    public static boolean hasUnreadMessages(int receiverId, int senderId) {
        return getUnreadCountForChat(receiverId, senderId) > 0;
    }

    /**
     * Get count of unique conversations with unread messages
     */
    public static int getUnreadConversationCount(int userId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            String query = "SELECT COUNT(DISTINCT sender_id) FROM message WHERE receiver_id = ? AND is_read = 0";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error getting unread conversation count: " + e.getMessage());
        }
        
        return 0;
    }
} 