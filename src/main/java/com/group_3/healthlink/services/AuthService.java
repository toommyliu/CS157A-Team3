package com.group_3.healthlink.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.group_3.healthlink.DatabaseMgr;

import java.sql.*;

public class AuthService {
    /**
     * Hashes a password using BCrypt.
     * @param password the password to hash
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean registerUser(
        String firstName,
        String lastName,
        String emailAddress,
        String password,
        String role
    ) {
        String hashedPassword = hashPassword(password);
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

        try {
            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());

            // String insertSql = "INSERT INTO USER (USERNAME, AGE, CREATED_DATE) "8
            //				          + "VALUES ('Mike Wu', 18, '" + sqlDate + "')";
            String query = "INSERT INTO user (email_address, password_hashed, first_name, last_name, role, created_at, updated_at)"
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, emailAddress);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, role);
            stmt.setDate(6, sqlDate);
            stmt.setDate(7, sqlDate);

            int rowCount = stmt.executeUpdate();
            stmt.close();
            return rowCount > 0;
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public static boolean doesUserExist(String email) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        try {
            String query = "SELECT COUNT(*) FROM user WHERE email_address = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count > 0;
            }
            rs.close();
            stmt.close();
            return false;
        } catch (Exception e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }
}
