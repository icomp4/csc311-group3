package org.compi.csc311group3.database;

import java.sql.*;

public class DbConnection {
    final static String DB_NAME="CSC311_Group3";
    final static String SERVER_URL = "jdbc:mysql://comperecsc311server.mysql.database.azure.com";
    final static String DB_URL = "jdbc:mysql://comperecsc311server.mysql.database.azure.com/"+DB_NAME;
    final static String USERNAME = "compereadmin";
    final static String PASSWORD = "Password123!";

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection serverConn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
             Statement stmt = serverConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        // Get connection
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        // Create table if it doesn't exist
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(200) NOT NULL,
                email VARCHAR(200),
                password VARCHAR(200)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTable);
        }

        return conn;
    }
}