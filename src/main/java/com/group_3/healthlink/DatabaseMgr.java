package com.group_3.healthlink;

import java.sql.*;

public class DatabaseMgr {
    private static DatabaseMgr instance;

    private final String DB_NAME = "healthlink";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "password";

    private java.sql.Connection connection;

    public DatabaseMgr() {
        try {
            // Use the new MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load JDBC driver. Is it in the classpath?");
            e.printStackTrace();
        }
    }

    // Connects to the database.
    private void createDbConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
            
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://localhost:3306/%s?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", DB_NAME),
                    DB_USERNAME,
                    DB_PASSWORD
            );
            System.out.println("Connected to the database successfully!");
        } catch (Exception e) {
            System.err.println("Could not connect to the database.");
            e.printStackTrace();
        }
    }

    // Returns the database connection.
    public java.sql.Connection getConnection() {
        try {
            // Check if connection is null, closed, or invalid
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(5)) {
                System.out.println("Connection is invalid or closed. Creating new connection...");
                createDbConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection validity: " + e.getMessage());
            createDbConnection();
        }
        
        return this.connection;
    }

    // Returns the singleton instance of DatabaseMgr.
    public static DatabaseMgr getInstance() {
        if (instance == null) {
            instance = new DatabaseMgr();
        }
        return instance;
    }
    
    // Close the connection (useful for cleanup)
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
