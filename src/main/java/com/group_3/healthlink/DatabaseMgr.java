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
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load JDBC driver. Is it in the classpath?");
            e.printStackTrace();
        }

        createDbConnection();
    }

    // Connects to the database.
    private void createDbConnection() {
        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://localhost:3306/%s?autoReconnect=true&useSSL=false", DB_NAME),
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
        if (this.connection == null) {
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
}