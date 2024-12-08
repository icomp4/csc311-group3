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


public class DepositService {
    private static UserService instance;
    private final User currentUser;
    private final DbConnection dbConnection = new DbConnection();

    public DepositService() {
        currentUser = UserService.getInstance().getCurrentUser();
    }

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
                            rs.getDouble("amount")
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
