package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.User;

public class DoctorService {
    public static User[] getPatients(int doctorId) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        String query = "SELECT * FROM assigned_to WHERE doctor_id = ?";

        try {
            java.sql.PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, doctorId);
            java.sql.ResultSet rs = stmt.executeQuery();

            java.util.List<User> patients = new java.util.ArrayList<>();
            while (rs.next()) {
                int patientId = rs.getInt("patient_id");
                User patient = AuthService.getUserById(patientId);
                if (patient != null) {
                    patients.add(patient);
                }
            }

            rs.close();
            stmt.close();
            return patients.toArray(new User[0]);
        } catch (Exception e) {
            System.err.println("Error getting patients: " + e.getMessage());
            return new User[0];
        }
    }
}
