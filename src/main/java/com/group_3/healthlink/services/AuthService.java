package com.group_3.healthlink.services;

import com.group_3.healthlink.DatabaseMgr;
import com.group_3.healthlink.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.*;

public class AuthService {
    /**
     * Hashes a password using BCrypt.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }

    public static int registerUser(
            String firstName,
            String lastName,
            String emailAddress,
            String password,
            String role) {
        String hashedPassword = hashPassword(password);
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();

        try {
            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());

            // String insertSql = "INSERT INTO USER (USERNAME, AGE, CREATED_DATE) "8
            // + "VALUES ('Mike Wu', 18, '" + sqlDate + "')";
            String query = "INSERT INTO user (email_address, password_hashed, first_name, last_name, role, created_at, updated_at)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, emailAddress);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, role);
            stmt.setDate(6, sqlDate);
            stmt.setDate(7, sqlDate);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Inserting user failed (no rows affected).");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    generatedKeys.close();
                    stmt.close();
                    return userId;
                } else {
                    throw new SQLException("Inserting user failed (no ID obtained).");
                }
            }
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the user's ID
     * @return a User object if found, or null if not found
     */
    public static User getUserById(int userId) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        try {
            String query = "SELECT * FROM user WHERE user_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getUser(stmt, rs, userId);
            } else {
                rs.close();
                stmt.close();
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error getUserById:" + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email address
     * @return a User object if found, or null if not found
     */
    public static User getUserByEmail(String email) {
        java.sql.Connection con = DatabaseMgr.getInstance().getConnection();
        try {
            String query = "SELECT * FROM user WHERE email_address = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                return getUser(stmt, rs, id);
            } else {
                rs.close();
                stmt.close();
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error getUserByEmail:" + e.getMessage());
            return null;
        }
    }

    private static User getUser(PreparedStatement stmt, ResultSet rs, int id) throws SQLException {
        String emailAddress = rs.getString("email_address");
        String passwordHashed = rs.getString("password_hashed");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String role = rs.getString("role");
        Date createdAt = rs.getDate("created_at");
        Date updatedAt = rs.getDate("updated_at");

        User user = new User();
        user.setId(id);
        user.setEmailAddress(emailAddress);
        user.setPasswordHashed(passwordHashed);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        rs.close();
        stmt.close();
        return user;
    }

    public static User ensureAuthenticated(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return user;
        }

        String userId = getUserIdFromCookie(request);
        if (userId != null) {
            try {
                int id = Integer.parseInt(userId);
                user = getUserById(id);
                if (user != null) {
                    System.out.println("Found user by id, from cookie");
                    request.getSession().setAttribute("user", user);
                    return user;
                }
            } catch (NumberFormatException e) {
            }
        }

        return null;
    }

    private static String getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
