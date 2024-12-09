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

        // Initialize all tables
        initializeTables(conn);

        return conn;
    }

    public void initializeTables(Connection conn) throws SQLException {

        // Method to initialize the user table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(200) NOT NULL UNIQUE,
                email VARCHAR(200) NOT NULL,
                password VARCHAR(200) NOT NULL
            );
            """;

        String createExpenseTable = """
                    CREATE TABLE IF NOT EXISTS expenses (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    description TEXT,
                    category VARCHAR(255) NOT NULL,
                    amount DECIMAL(10, 2) NOT NULL
                    );
                    """;

        String createCategoriesTables = """
                   CREATE TABLE IF NOT EXISTS categories(
                   id INT AUTO_INCREMENT PRIMARY KEY,
                   name VARCHAR(255) UNIQUE NOT NULL
                   );
                   """;

        String createDepositsTable = """
                    CREATE TABLE IF NOT EXISTS deposits (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    amount DECIMAL(10, 2) NOT NULL,
                    user_id INT NOT NULL,
                    account_type VARCHAR(255) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                    );
                    """;

        try (Statement stmt = conn.createStatement()) {

            // Execute the statements
            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createExpenseTable);
            stmt.executeUpdate(createCategoriesTables);
            stmt.executeUpdate(createDepositsTable);
            System.out.println("Database tables initialized successfully.");
        }
    }
}