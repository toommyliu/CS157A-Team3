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

    public static boolean doesUserExist(String email) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE email= " + "'" + email + "'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
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
