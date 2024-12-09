package org.compi.csc311group3.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AnalyticsDAO {
    private final Connection conn;

    /**
     *
     * @param conn
     */
    public AnalyticsDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     *
     * @return
     * @throws SQLException
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

    public int getNumberOfEntries(String category, Timestamp startDate, Timestamp endDate) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM expenses WHERE category = ? AND date_time BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);
            ResultSet rs = stmt.executeQuery();
            return  rs.next() ? rs.getInt("count") : 0;
        }
    }

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
