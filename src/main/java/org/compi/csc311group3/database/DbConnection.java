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

        // Create table if it doesn't exist //made change to make the username unique
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(200) NOT NULL UNIQUE,
                email VARCHAR(200),
                password VARCHAR(200)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTable);
        }

        return conn;
    }
    // Method to initialize the expense table
    /**+----+--------+-------------+-------------------------+---------------------+---------+
     | id | amount | type        | description             | expense_date        | user_id |
     +----+--------+-------------+-------------------------+---------------------+---------+
     |  1 |  25.50 | Food        | Lunch at a restaurant   | 2024-11-20 12:34:56 |       1 |
     |  2 |  15.00 | Transport   | Bus ticket              | 2024-11-19 08:22:10 |       2 |
     |  3 | 100.00 | Utilities   | Electricity bill        | 2024-11-18 17:00:00 |       1 |
     |  4 |  50.00 | Entertainment | Movie tickets          | 2024-11-20 19:45:00 |       3 |
     |  5 |  75.00 | Food        | Grocery shopping        | 2024-11-17 14:30:45 |       2 |
     +----+--------+-------------+-------------------------+---------------------+---------+
*/
     public void initializeExpenseTable() {
        String createExpensesTable = """
        CREATE TABLE IF NOT EXISTS expenses (
            id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
            amount DECIMAL(10, 2) NOT NULL,
            type VARCHAR(100) NOT NULL,
            description TEXT,
            expense_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            user_id INT(10),
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createExpensesTable);
            System.out.println("Expenses table initialized successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}