package org.compi.csc311group3.service;

import org.compi.csc311group3.database.DbConnection;
import org.compi.csc311group3.model.Deposit;
import org.compi.csc311group3.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DepositService class provides methods to add and retrieve deposits from the database.
 * This class is used to interact with the deposits table in the database.
 */
public class DepositService {

    // singleton instance of the UserService class
    private static UserService instance;

    // the currently logged-in user
    private final User currentUser;

    // singleton instance of the DbConnection class
    private final DbConnection dbConnection = new DbConnection();

    /**
     * Constructor to initialize the currently logged-in user.
     */
    public DepositService() {
        currentUser = UserService.getInstance().getCurrentUser();
    }

    /**
     * Method to add a deposit to the database.
     * @param account the account type to which the deposit is made
     * @param amount the amount of the deposit
     */
    public void addDeposit(String account, double amount) {
        String sql = "INSERT INTO deposits (user_id, account_type, amount) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, account);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
            System.out.println("Deposit added successfully to " + account + " account");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get all deposits made by the currently logged-in user.
     * @return a list of deposits made by the user
     */
    public List<Deposit> getDeposits() {
        String query = "SELECT * FROM deposits WHERE user_id = ?";
        List<Deposit> deposits = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, currentUser.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Deposit deposit = new Deposit(
                            rs.getInt("id"),
                            rs.getString("account_type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("date_time")
                    );
                    deposits.add(deposit);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deposits;
    }
}
