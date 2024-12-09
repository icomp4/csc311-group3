package org.compi.csc311group3.model;

import java.sql.Timestamp;

/**
 * Deposit class represents a deposit made by a user.
 * This class is used to store deposit information in the database.
 */
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
