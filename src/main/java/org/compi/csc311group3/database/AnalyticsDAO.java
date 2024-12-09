package org.compi.csc311group3.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for Analytics database object
 */
public class AnalyticsDAO {
    private final Connection conn;

    /**
     * Constructor for AnalyticsDAO
     * Initializes DAO with a database connection
     * @param conn The connection object used to interact with the database
     */
    public AnalyticsDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Gets a list of unique expense categories form the database
     * @return A list of category expense names
     * @throws SQLException If a database access error occurs
     */
    public List<String> getCategories() throws SQLException {
        String query = "SELECT DISTINCT name FROM categories";
        List<String> categories = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        }
        return categories;
    }

    /**
     * Calculates the total expense for a given category with a selected data period
     * @param category The category for which the total is calculated
     * @param startDate The start date of the date period
     * @param endDate The end date of the date period
     * @return The total expense amount
     * @throws SQLException If a database access error occurs
     */
    public double getTotalExpense(String category, Timestamp startDate,Timestamp endDate) throws SQLException {
        String query = "SELECT SUM(amount) AS total FROM expenses WHERE category = ? AND date_time BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    /**
     * Compares the total expenses for two periods and a specified category
     * @param category The category to compare
     * @param startDate1 The start date of the first period
     * @param endDate1 The end date of the first period
     * @param startDate2 The start date of the second period
     * @param endDate2 The end date of the second period
     * @return The total difference in expenses between the two periods
     * @throws SQLException If a database access error occurs
     */
    public double[] compareExpenses(String category, Timestamp startDate1, Timestamp endDate1, Timestamp startDate2, Timestamp endDate2) throws SQLException {
        String query = "SELECT SUM(amount) AS total FROM expenses WHERE category = ? AND date_time BETWEEN ? AND ?";
        double total1 = 0;
        double total2 = 0;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate1);
            stmt.setTimestamp(3, endDate1);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) total1 = rs.getDouble("total");
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate2);
            stmt.setTimestamp(3, endDate2);
            rs = stmt.executeQuery();
            if(rs.next()) total2 = rs.getDouble("total");
        }
        return new double[]{total1, total2};
    }

    /**
     * Gets the number of expense entries grouped by a date within a specified date range
     * @param category The category to be counted
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return A list of maps, where each map contains the date and the count of entries for that date
     * @throws SQLException If a database access error occurs
     */
    public List <Map<String, Object>> getNumberOfEntriesByDate(String category, Timestamp startDate, Timestamp endDate) throws SQLException {
        String query = "SELECT DATE(date_time) AS entry_date, COUNT(*) AS entry_count " +
                "FROM expenses " +
                "WHERE category = ? AND date_time BETWEEN ? AND ? " +
                "GROUP BY entry_date " +
                "ORDER BY entry_date";

        List<Map<String, Object>> result = new ArrayList<>();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("date", rs.getString("entry_date"));
                row.put("count", rs.getInt("entry_count"));
                result.add(row);
            }
        }
        return result;
    }

    /**
     * Gets the toital expenses grouped by category within a specified date range
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return A map where the key is the category name and the value is the total expense
     * @throws SQLException If a database access error occurs
     */
    public Map<String, Double> getExpenseByCategory(Timestamp startDate, Timestamp endDate) throws SQLException{
      String query = "SELECT category, SUM(amount) AS total FROM expenses WHERE date_time BETWEEN ? AND ? GROUP BY category";
      Map<String, Double> categoryAnalysis = new HashMap<>();
      try (PreparedStatement stmt = conn.prepareStatement(query)) {
          stmt.setTimestamp(1, startDate);
          stmt.setTimestamp(2, endDate);
          ResultSet rs = stmt.executeQuery();
          while(rs.next()) {
              categoryAnalysis.put(rs.getString("category"), rs.getDouble("total"));
          }
      }
      return categoryAnalysis;
    }

    /**
     * Gets all  expenses for a specified category within a given date range
     * Each expense includes the ate, amount, and description
     * @param category The category to filter by
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return A list of Maps, where each map represents an expense with keys
     * @throws SQLException If a database access error occurs
     */
    public List<Map<String, Object>> getExpensesInPeriod(String category, Timestamp startDate, Timestamp endDate) throws SQLException{
        String query = "SELECT date_time, amount, description FROM expenses WHERE category = ? AND date_time BETWEEN ? AND ?";
        List<Map<String, Object>> expenses = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Map<String, Object> expense = new HashMap<>();
                expense.put("date", rs.getTimestamp("date_time"));
                expense.put("amount", rs.getDouble("amount"));
                expense.put("description", rs.getString("description"));
                expenses.add(expense);
            }
        }
        return expenses;
    }
}
