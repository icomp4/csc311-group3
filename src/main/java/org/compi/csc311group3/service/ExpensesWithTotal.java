package org.compi.csc311group3.service;

import org.compi.csc311group3.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/*
* This class is used to access information about the uses expenses from the ExpenseDAO
* */
public class ExpensesWithTotal {

    private List<Expense> expenses;
    private double totalExpenseAmount;
    private Map<LocalDate, Double> dailyExpenses; //stores the daily expenses

    public ExpensesWithTotal(List<Expense> expenses, double totalAmount, Map<LocalDate, Double> dailyExpenses) {
        this.expenses = expenses;
        this.totalExpenseAmount = totalAmount;
        this.dailyExpenses = dailyExpenses;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public Map<LocalDate, Double> getDailyExpenses() {
        return dailyExpenses; //returns the daily expenses map
    }

}
