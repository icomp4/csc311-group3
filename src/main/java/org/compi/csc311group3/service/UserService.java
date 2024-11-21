package org.compi.csc311group3.service;

import org.compi.csc311group3.database.DbConnection;
import org.compi.csc311group3.model.User;

import java.sql.*;

/**
 * The user service
 * This class is responsible for handling user related operations that interact with the database
 * It has a field of type DbConnection to connect to the database
 */
public class UserService {
    private static UserService instance; // The single instance
    private User currentUser;

    // Private constructor to prevent instantiation from outside
    private UserService() {}

    // Method to get the instance of UserService
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService(); // Create the instance if it's not created yet
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Setter for currentUser
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    DbConnection dbConnection = new DbConnection();


    /**
     * Sign up method
     * @param email the email used to sign up
     * @param username the username used to sign up
     * @param password the password used to sign up
     */
    public void SignUp(String email, String username, String password) {
        String sql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            System.out.println("User registered successfully with username: " + username);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Login method
     * @param username the username used to log in
     * @param password the password used to log in
     * @return true if the account with the given username exists, and the password matches
     */
    public boolean Login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("email"), rs.getString("username"), rs.getString("password"));
                user.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (user != null && user.getPassword().equals(password)) {

            this.currentUser = user;
            System.out.println("Login successful. Current user ID: " + currentUser.getId());
            return true;
        } else {
            return false;
        }
    }

    // Initializes the database and the expenses table
    public void initializeDatabase() {
        dbConnection.initializeExpenseTable();
    }
    /**
     * Add an expense for the current user.
     * @param amount the amount spent
     * @param type the type of expense (e.g., "Food", "Utilities", etc.)
     * @param description an optional description of the expense
     */
    public void addExpense(double amount, String type, String description) {
        System.out.println("Login successful. Current user ID: " + currentUser.getId());
        if (currentUser == null) {
            System.out.println("No user logged in.");
            return;
        }

        String sql = "INSERT INTO expenses (user_id, amount, type, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.getId());  // Assuming User has an `id` field
            pstmt.setDouble(2, amount);
            pstmt.setString(3, type);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
            System.out.println("Expense added successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Remove an expense by its ID for the current user.
     * @param expenseId the ID of the expense to be removed
     */
    public void removeExpense(int expenseId) {
        if (currentUser == null) {
            System.out.println("No user logged in.");
            return;
        }

        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            pstmt.setInt(2, currentUser.getId());  // Ensure the expense belongs to the current user
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense removed successfully.");
            } else {
                System.out.println("Expense not found or not associated with the current user.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void printAllExpenses() {
        String sql = "SELECT * FROM expenses";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Print column headers
            System.out.println("+----+--------+-------------+-------------------------+---------------------+---------+");
            System.out.println("| id | amount | type        | description             | expense_date        | user_id |");
            System.out.println("+----+--------+-------------+-------------------------+---------------------+---------+");

            // Loop through the result set and print each row
            while (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");
                String description = rs.getString("description");
                Timestamp expenseDate = rs.getTimestamp("expense_date");
                int userId = rs.getInt("user_id");

                // Print the expense record
                System.out.printf("| %-2d | %-6.2f | %-11s | %-23s | %-19s | %-7d |\n",
                        id, amount, type, description, expenseDate.toString(), userId);
            }

            // Print table footer
            System.out.println("+----+--------+-------------+-------------------------+---------------------+---------+");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
