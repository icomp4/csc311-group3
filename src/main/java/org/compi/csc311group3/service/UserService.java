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
}
