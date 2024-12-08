package org.compi.csc311group3.service;

import org.compi.csc311group3.Expense;

import java.util.List;

/*
* This class is used to access information about the uses expenses from the ExpenseDAO
* */
public class ExpensesWithTotal {

    private List<Expense> expenses;
    private double totalExpenseAmount;

    public ExpensesWithTotal(List<Expense> expenses, double totalAmount) {
        this.expenses = expenses;
        this.totalExpenseAmount = totalAmount;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

}
