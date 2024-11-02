package org.compi.csc311group3;

import java.time.LocalDateTime;

public class Expense {
    private String description;
    private String category;
    private LocalDateTime dateTime;
    private double amount;

    public Expense(String description, String category, LocalDateTime dateTime, double amount) {
        this.description = description;
        this.category = category;
        this.dateTime = dateTime;
        this.amount = amount;
        }

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

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) { this.amount = amount; }

        @Override
        public String toString() {
            return String.format("%s: %s, %s, %.2f", description, category, dateTime, amount);
        }
}
