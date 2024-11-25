package org.compi.csc311group3;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private LocalDateTime date_time;
    private String description;
    private String category;
    private double amount;

    public Expense(LocalDateTime date_time, String description, String category, double amount) {
        this.date_time = date_time;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public Expense(int id, LocalDateTime date_time, String description, String category, double amount) {
        this.id = id;
        this.date_time = date_time;
        this.description = description;
        this.category = category;
        this.amount = amount;
        }

        public int getId(){return id;}

        public void setId(int id){this.id = id;}

        public String getDescription() {return description;}

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public LocalDateTime getDate_time() {
            return date_time;
        }

        public void setDate_time(LocalDateTime date_time) {
            this.date_time = date_time;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) { this.amount = amount; }

        @Override
        public String toString() {
            return String.format("%s: %s, %s, %.2f", id, date_time, description, category, amount);
        }
}
