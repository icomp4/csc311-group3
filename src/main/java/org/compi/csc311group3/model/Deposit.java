package org.compi.csc311group3.model;

import java.sql.Timestamp;

public class Deposit {
    private int id;
    private String account;
    private double amount;
    private Timestamp dateTime;


    public Deposit(int id, String account, double amount, Timestamp dateTime) {
        this.id = id;
        this.account = account;
        this.amount = amount;
        this.dateTime = dateTime;
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

    public Timestamp getDateTime() {return dateTime;}
}
