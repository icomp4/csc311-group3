package org.compi.csc311group3.model;

public class Deposit {
    private int id;
    private String account;
    private double amount;

    public Deposit(int id, String account, double amount) {
        this.id = id;
        this.account = account;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }
}
