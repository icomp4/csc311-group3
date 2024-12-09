package org.compi.csc311group3;

import java.time.LocalDateTime;

/**
 * Expense class which represents an expense entry with attributes
 */
public class Expense {
    private int id;
    private LocalDateTime date_time;
    private String description;
    private String category;
    private double amount;

    /**
     * Constructs an expense object with specified attributes
     * @param date_time The date and time of the expense
     * @param description A description of the expense
     * @param category The category of the expense
     * @param amount The amount of the expense
     */
    public Expense(LocalDateTime date_time, String description, String category, double amount) {
        this.date_time = date_time;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    /**
     * Constructs an expense object with the specified attributes
     * @param id The unique ID of the expense
     * @param date_time The date and time of the expense
     * @param description A description of the expense
     * @param category The category of the expense
     * @param amount The amount of the expense
     */
    public Expense(int id, LocalDateTime date_time, String description, String category, double amount) {
        this.id = id;
        this.date_time = date_time;
        this.description = description;
        this.category = category;
        this.amount = amount;
        }

    /**
     * Gets the unique id of the expense
     * @return The expense id
     */
    public int getId(){return id;}

    /**
     * Sets the unique id of the expense
     * @param id The expense id
     */
    public void setId(int id){this.id = id;}

    /**
     * Gets the description of the expense
     * @return The expense description
     */
    public String getDescription() {return description;}

    /**
     * Sets the description of the expense
     * @param description The expense description
     */
    public void setDescription(String description) {
            this.description = description;
    }

    /**
     * Gets the category of the expense
     * @return The expense description
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense
     * @param category The expense category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the date and time of the expense
     * @return The date and time of the expense
     */
    public LocalDateTime getDate_time() {
        return date_time;
    }

    /**
     * Sets the date and time of the expense
     * @param date_time The date and time of the expense
     */
    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }

    /**
     * Gets the amount of the expense
     * @return The amount of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense
     * @param amount The amount of the expense
     */
    public void setAmount(double amount) { this.amount = amount; }

    /**
     * Creates a string representation of an expense
     * @return A formatted string containing the expense attributes
     */
    @Override
    public String toString() {
        return String.format("%s: %s, %s, %.2f", id, date_time, description, category, amount);
    }
}
