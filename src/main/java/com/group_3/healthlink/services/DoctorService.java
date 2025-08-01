package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.Patient;
import com.group_3.healthlink.Doctor;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.Note;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    public static Doctor getByUserId(int userId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT d.doctor_id, d.department, d.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM doctor d JOIN user u ON d.user_id = u.user_id " +
                "WHERE d.user_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = getDoctor(rs);
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
                "FROM doctor d JOIN user u ON d.user_id = u.user_id " +
                "WHERE d.doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = getDoctor(rs);

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
                patient.setRole(UserRole.Patient);
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

    public static Doctor[] getAll() {
        Connection con = DatabaseMgr.getInstance().getConnection();

        String query = "SELECT d.doctor_id, d.department, d.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM doctor d JOIN user u ON d.user_id = u.user_id";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<Doctor> doctors = new ArrayList<>();
            while (rs.next()) {
                Doctor doctor = getDoctor(rs);
                if (doctor != null)
                    doctors.add(doctor);
            }

            rs.close();
            stmt.close();
            return doctors.toArray(new Doctor[0]);
        } catch (Exception e) {
            System.err.println("Error getAll: " + e.getMessage());
            return new Doctor[0];
        }
    }

    public static Doctor[] getByDepartment(String department) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT d.doctor_id, d.department, d.user_id, " +
                "u.first_name, u.last_name, u.email_address, u.role, u.created_at, u.updated_at " +
                "FROM doctor d JOIN user u ON d.user_id = u.user_id " +
                "WHERE d.department = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            List<Doctor> doctors = new ArrayList<>();
            while (rs.next()) {
                Doctor doctor = getDoctor(rs);
                if (doctor != null)
                    doctors.add(doctor);
            }

            rs.close();
            stmt.close();
            return doctors.toArray(new Doctor[0]);
        } catch (Exception e) {
            System.err.println("Error getByDepartment: " + e.getMessage());
            return new Doctor[0];
        }
    }

    public static boolean createNew(int userId, String department) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "INSERT INTO doctor (department, user_id) VALUES (?, ?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, department);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating new doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the record of a doctor in the database.
     * @param doctorId The doctor id
     * @param department The new department of the doctor
     * @return Whether the update was successful or not.
     */
    public static boolean updateDoctor(int doctorId, String department) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "UPDATE doctor SET department = ? WHERE doctor_id = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, department);
            stmt.setInt(2, doctorId);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the user information for a doctor.
     * @param userId The user id
     * @param firstName The new first name
     * @param lastName The new last name
     * @param email The new email
     * @param password The new password (optional, only if not null/empty)
     * @return Whether the update was successful or not.
     */
    public static boolean updateUser(int userId, String firstName, String lastName, String email, String password) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        String query;
        
        if (password != null && !password.trim().isEmpty()) {
            query = "UPDATE user SET first_name = ?, last_name = ?, email_address = ?, password = ? WHERE user_id = ?";
        } else {
            query = "UPDATE user SET first_name = ?, last_name = ?, email_address = ? WHERE user_id = ?";
        }

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(4, password);
                stmt.setInt(5, userId);
            } else {
                stmt.setInt(4, userId);
            }

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a doctor and their associated user account.
     * @param doctorId The doctor id
     * @param userId The user id
     * @return Whether the deletion was successful or not.
     */
    public static boolean deleteDoctor(int doctorId, int userId) {
        Connection con = DatabaseMgr.getInstance().getConnection();
        
        try {
            con.setAutoCommit(false);
            
            // First delete the doctor record
            String deleteDoctorQuery = "DELETE FROM doctor WHERE doctor_id = ?";
            PreparedStatement deleteDoctorStmt = con.prepareStatement(deleteDoctorQuery);
            deleteDoctorStmt.setInt(1, doctorId);
            int doctorRowsAffected = deleteDoctorStmt.executeUpdate();
            deleteDoctorStmt.close();
            
            // Then delete the user record
            String deleteUserQuery = "DELETE FROM user WHERE user_id = ?";
            PreparedStatement deleteUserStmt = con.prepareStatement(deleteUserQuery);
            deleteUserStmt.setInt(1, userId);
            int userRowsAffected = deleteUserStmt.executeUpdate();
            deleteUserStmt.close();
            
            con.commit();
            con.setAutoCommit(true);
            
            return doctorRowsAffected > 0 && userRowsAffected > 0;
        } catch (SQLException e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }

    private static Doctor getDoctor(ResultSet rs) throws SQLException {
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
        return doctor;
    }

    public static List<Note> getNotesByDoctorId(int doctorId) {
        List<Note> notes = new ArrayList<>();
        try {
            Connection con = DatabaseMgr.getInstance().getConnection();
            String query = "SELECT * FROM notes WHERE doctor_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("note_id"));
                note.setContent(rs.getString("note_content"));
                note.setPatientId(rs.getInt("patient_id"));
                note.setDoctorId(rs.getInt("doctor_id"));
                note.setCreatedAt(rs.getTimestamp("created_at"));
                notes.add(note);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Error fetching doctor notes: " + e.getMessage());
        }
        return notes;
    }
}
