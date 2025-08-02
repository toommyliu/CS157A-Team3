package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.TestResult;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.services.PatientService;
import com.group_3.healthlink.services.DoctorService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestResultService {
    
    // Upload a new test result
    public static boolean uploadTestResult(int patientId, int doctorId, String fileName, 
                                         byte[] fileData, String fileType, String description) {
        String sql = "INSERT INTO test_results (patient_id, doctor_id, file_name, file_data, file_type, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, fileName);
            stmt.setBytes(4, fileData);
            stmt.setString(5, fileType);
            stmt.setString(6, description);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get test results for a patient
    public static List<TestResult> getTestResultsByPatientId(int patientId) {
        List<TestResult> results = new ArrayList<>();
        String sql = "SELECT tr.*, CONCAT(u1.first_name, ' ', u1.last_name) as patient_name, " +
                    "CONCAT(u2.first_name, ' ', u2.last_name) as doctor_name " +
                    "FROM test_results tr " +
                    "JOIN patient p ON tr.patient_id = p.patient_id " +
                    "JOIN doctor d ON tr.doctor_id = d.doctor_id " +
                    "JOIN user u1 ON p.user_id = u1.user_id " +
                    "JOIN user u2 ON d.user_id = u2.user_id " +
                    "WHERE tr.patient_id = ? " +
                    "ORDER BY tr.upload_date DESC";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setPatientId(rs.getInt("patient_id"));
                result.setDoctorId(rs.getInt("doctor_id"));
                result.setFileName(rs.getString("file_name"));
                result.setFileType(rs.getString("file_type"));
                result.setUploadDate(rs.getTimestamp("upload_date"));
                result.setDescription(rs.getString("description"));
                result.setPatientName(rs.getString("patient_name"));
                result.setDoctorName(rs.getString("doctor_name"));
                
                results.add(result);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    // Get test results for a specific patient-doctor combination
    public static List<TestResult> getTestResultsByPatientAndDoctor(int patientId, int doctorId) {
        List<TestResult> results = new ArrayList<>();
        String sql = "SELECT tr.*, CONCAT(u1.first_name, ' ', u1.last_name) as patient_name, " +
                    "CONCAT(u2.first_name, ' ', u2.last_name) as doctor_name " +
                    "FROM test_results tr " +
                    "JOIN patient p ON tr.patient_id = p.patient_id " +
                    "JOIN doctor d ON tr.doctor_id = d.doctor_id " +
                    "JOIN user u1 ON p.user_id = u1.user_id " +
                    "JOIN user u2 ON d.user_id = u2.user_id " +
                    "WHERE tr.patient_id = ? AND tr.doctor_id = ? " +
                    "ORDER BY tr.upload_date DESC";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setPatientId(rs.getInt("patient_id"));
                result.setDoctorId(rs.getInt("doctor_id"));
                result.setFileName(rs.getString("file_name"));
                result.setFileType(rs.getString("file_type"));
                result.setUploadDate(rs.getTimestamp("upload_date"));
                result.setDescription(rs.getString("description"));
                result.setPatientName(rs.getString("patient_name"));
                result.setDoctorName(rs.getString("doctor_name"));
                
                results.add(result);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    // Get test results for a doctor (all patients' results)
    public static List<TestResult> getTestResultsByDoctorId(int doctorId) {
        List<TestResult> results = new ArrayList<>();
        String sql = "SELECT tr.*, CONCAT(u1.first_name, ' ', u1.last_name) as patient_name, " +
                    "CONCAT(u2.first_name, ' ', u2.last_name) as doctor_name " +
                    "FROM test_results tr " +
                    "JOIN patient p ON tr.patient_id = p.patient_id " +
                    "JOIN doctor d ON tr.doctor_id = d.doctor_id " +
                    "JOIN user u1 ON p.user_id = u1.user_id " +
                    "JOIN user u2 ON d.user_id = u2.user_id " +
                    "WHERE tr.doctor_id = ? " +
                    "ORDER BY tr.upload_date DESC";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setPatientId(rs.getInt("patient_id"));
                result.setDoctorId(rs.getInt("doctor_id"));
                result.setFileName(rs.getString("file_name"));
                result.setFileType(rs.getString("file_type"));
                result.setUploadDate(rs.getTimestamp("upload_date"));
                result.setDescription(rs.getString("description"));
                result.setPatientName(rs.getString("patient_name"));
                result.setDoctorName(rs.getString("doctor_name"));
                
                results.add(result);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    // Get a specific test result by ID
    public static TestResult getTestResultById(int resultId) {
        String sql = "SELECT tr.*, CONCAT(u1.first_name, ' ', u1.last_name) as patient_name, " +
                    "CONCAT(u2.first_name, ' ', u2.last_name) as doctor_name " +
                    "FROM test_results tr " +
                    "JOIN patient p ON tr.patient_id = p.patient_id " +
                    "JOIN doctor d ON tr.doctor_id = d.doctor_id " +
                    "JOIN user u1 ON p.user_id = u1.user_id " +
                    "JOIN user u2 ON d.user_id = u2.user_id " +
                    "WHERE tr.result_id = ?";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                TestResult result = new TestResult();
                result.setResultId(rs.getInt("result_id"));
                result.setPatientId(rs.getInt("patient_id"));
                result.setDoctorId(rs.getInt("doctor_id"));
                result.setFileName(rs.getString("file_name"));
                result.setFileData(rs.getBytes("file_data"));
                result.setFileType(rs.getString("file_type"));
                result.setUploadDate(rs.getTimestamp("upload_date"));
                result.setDescription(rs.getString("description"));
                result.setPatientName(rs.getString("patient_name"));
                result.setDoctorName(rs.getString("doctor_name"));
                
                return result;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Delete a test result
    public static boolean deleteTestResult(int resultId) {
        String sql = "DELETE FROM test_results WHERE result_id = ?";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if user has access to a test result
    public static boolean hasAccess(int userId, UserRole userRole, int resultId) {
        TestResult result = getTestResultById(resultId);
        if (result == null) return false;
        
        if (userRole == UserRole.Admin) return true;
        
        if (userRole == UserRole.Patient) {
            // Patient can only access their own results
            // Need to get patient_id from user_id
            try {
                int patientId = PatientService.getByUserId(userId).getPatientId();
                return result.getPatientId() == patientId;
            } catch (Exception e) {
                return false;
            }
        }
        
        if (userRole == UserRole.Doctor) {
            // Doctor can access results where they are the assigned doctor
            // Need to get doctor_id from user_id
            try {
                int doctorId = DoctorService.getByUserId(userId).getDoctorId();
                return result.getDoctorId() == doctorId;
            } catch (Exception e) {
                return false;
            }
        }
        
        return false;
    }
    
    // Get assigned doctors for a patient
    public static List<Integer> getAssignedDoctorIds(int patientId) {
        List<Integer> doctorIds = new ArrayList<>();
        String sql = "SELECT doctor_id FROM assigned_to WHERE patient_id = ?";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctorIds.add(rs.getInt("doctor_id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return doctorIds;
    }
    
    // Get patient summary with test result counts for doctor
    public static List<PatientTestSummary> getPatientTestSummaryForDoctor(int doctorId) {
        List<PatientTestSummary> summaries = new ArrayList<>();
        String sql = "SELECT p.patient_id, CONCAT(u.first_name, ' ', u.last_name) as patient_name, " +
                    "COUNT(tr.result_id) as file_count " +
                    "FROM patient p " +
                    "JOIN user u ON p.user_id = u.user_id " +
                    "JOIN assigned_to at ON p.patient_id = at.patient_id " +
                    "LEFT JOIN test_results tr ON p.patient_id = tr.patient_id AND tr.doctor_id = ? " +
                    "WHERE at.doctor_id = ? " +
                    "GROUP BY p.patient_id, u.first_name, u.last_name " +
                    "ORDER BY u.first_name, u.last_name";
        
        try (Connection conn = DatabaseMgr.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            stmt.setInt(2, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PatientTestSummary summary = new PatientTestSummary();
                summary.setPatientId(rs.getInt("patient_id"));
                summary.setPatientName(rs.getString("patient_name"));
                summary.setFileCount(rs.getInt("file_count"));
                summaries.add(summary);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return summaries;
    }
    
    // Inner class for patient test summary
    public static class PatientTestSummary {
        private int patientId;
        private String patientName;
        private int fileCount;
        
        // Getters and setters
        public int getPatientId() { return patientId; }
        public void setPatientId(int patientId) { this.patientId = patientId; }
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        public int getFileCount() { return fileCount; }
        public void setFileCount(int fileCount) { this.fileCount = fileCount; }
    }
} 