package org.compi.csc311group3.view.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.compi.csc311group3.database.AnalyticsDAO;
import org.compi.csc311group3.database.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsController {

    @FXML
    private DatePicker period1Start, period1End, period2Start, period2End;

    @FXML
    private ComboBox<String> expenseComboBox;

    @FXML
    private Button totalExpenseButton, compareExpenseButton, numberOfEntriesButton, categoryAnalysisButton, clearButton;

    @FXML
    private Pane reportDisplayPane;

    private AnalyticsDAO analyticsDAO;

    public void initialize() {

        period1End.setDisable(true);
        period2Start.setDisable(true);
        period2End.setDisable(true);

        totalExpenseButton.setDisable(true);
        compareExpenseButton.setDisable(true);
        numberOfEntriesButton.setDisable(true);
        categoryAnalysisButton.setDisable(true);

        setupFieldListeners();

        try{
            Connection conn = new DbConnection().getConnection();
            analyticsDAO = new AnalyticsDAO(conn);

            List<String> categories = analyticsDAO.getCategories();
            expenseComboBox.getItems().addAll(categories);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateButtonStates();
    }

    @FXML
    private void calculateTotalExpense(){
        try{
            String category = getSelectedCategory();
            if(category == null || category.isEmpty()){
                displayError("Please select a category");
                return;
            }

            Timestamp startDate = getTimestampFromDatePicker(period1Start);
            Timestamp endDate = getTimestampFromDatePicker(period1End);
            if(startDate == null || endDate == null){
                displayError("Please select a valid start and end date");
                return;
            }

            double total = analyticsDAO.getTotalExpense(category, startDate, endDate);
            displayReport("Total spent on " + category + ": $" + total);
            List<Map<String, Object>> expenses = analyticsDAO.getExpensesInPeriod(category, startDate, endDate);

            if(expenses.isEmpty()){
                displayError("No expenses found for the selected category or period");
                return;
            }

            displayExpensesInTable(expenses, total);
        } catch (Exception e) {
            displayError("Error calculating total expense.");
            e.printStackTrace();
        }
    }

    @FXML
    private void compareExpense(){
        try{
            String category = getSelectedCategory();
            if(category == null){
                return;
            }
            Timestamp startDate1 = getTimestampFromDatePicker(period1Start);
            Timestamp endDate1 = getTimestampFromDatePicker(period1End);
            Timestamp startDate2 = getTimestampFromDatePicker(period2Start);
            Timestamp endDate2 = getTimestampFromDatePicker(period2End);

            if(startDate1 == null || endDate1 == null || startDate2 == null || endDate2 == null){
                return;
            }
            double total1 = analyticsDAO.getTotalExpense(category, startDate1, endDate1);
            double total2 = analyticsDAO.getTotalExpense(category, startDate2, endDate2);

            List<Map<String, Object>> period1Expenses = analyticsDAO.getExpensesInPeriod(category, startDate1, endDate1);
            List<Map<String,Object>> period2Expenses = analyticsDAO.getExpensesInPeriod(category, startDate2, endDate2);

            double difference = total2 - total1;

            List<Map<String, Object>> combinedData = new ArrayList<>();
            combinedData.addAll(period1Expenses);

            Map<String, Object> period1TotalRow = new HashMap<>();
            period1TotalRow.put("date", "");
            period1TotalRow.put("amount", total1);
            period1TotalRow.put("description", "Period 1 Total");
            combinedData.add(period1TotalRow);

            combinedData.add(new HashMap<>());
            combinedData.addAll(period2Expenses);

            Map<String, Object> period2TotalRow = new HashMap<>();
            period2TotalRow.put("date", "");
            period2TotalRow.put("amount", total2);
            period2TotalRow.put("description", "Period 2 Total");
            combinedData.add(period2TotalRow);

            Map<String, Object> differenceRow = new HashMap<>();
            differenceRow.put("date", "");
            differenceRow.put("amount", difference);
            differenceRow.put("description", "Difference");
            combinedData.add(differenceRow);

            displayComparisonTable(combinedData);
        } catch (Exception e) {
            displayError("Error comparing expense.");
            e.printStackTrace();
        }
    }

    @FXML
    private void calculateNumberOfEntries(){
        try{
            String category = getSelectedCategory();
            if(category == null){
                return;
            }

            Timestamp startDate = getTimestampFromDatePicker(period1Start);
            Timestamp endDate = getTimestampFromDatePicker(period1End);

            if(startDate == null || endDate == null){
                return;
            }

            int count = analyticsDAO.getNumberOfEntries(category, startDate, endDate);
            displayReport("Number of entries for " + category + ": " + count);
        } catch (Exception e) {
            displayError("Error calculating number of entries.");
            e.printStackTrace();
        }
    }

    @FXML
    private void showEntriesGraph(){
        String category = expenseComboBox.getValue();
        if(category == null || category.isEmpty()){
            displayError("Please select a valid category");
            return;
        }

        Timestamp startDate = getTimestampFromDatePicker(period1Start);
        Timestamp endDate = getTimestampFromDatePicker(period1End);

        if(startDate == null || endDate == null){
            displayError("Please select a valid start and end date");
            return;
        }
        try{
            List<Map<String, Object>> entriesData = analyticsDAO.getNumberOfEntriesByDate(category, startDate, endDate);
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            LineChart<String,Number> lineChart = new LineChart<>(xAxis, yAxis);
            xAxis.setLabel("Category");
            yAxis.setLabel("Number of Entries");
            lineChart.setTitle("Number of Entries Over Time Period");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Entries");

            for(Map<String, Object> entry : entriesData){
                String date = entry.get("date").toString();
                int count = (int) entry.get("count");
                series.getData().add(new XYChart.Data<>(date, count));
            }
            lineChart.getData().add(series);

            displayLineReport(lineChart);
        } catch (Exception e) {
            displayError("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    private void calculateCategoryAnalysis(){
        try {
            Timestamp startDate = getTimestampFromDatePicker(period1Start);
            Timestamp endDate = getTimestampFromDatePicker(period1End);
            if (startDate == null || endDate == null) {
                return;
            }
            Map<String, Double> categoryData = analyticsDAO.getExpenseByCategory(startDate, endDate);

            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Category");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Amount in $");
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Expense Category Analysis");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Expense Category Analysis");
            for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series);
            reportDisplayPane.getChildren().clear();
            reportDisplayPane.getChildren().add(barChart);
        } catch (Exception e) {
            displayError("Error calculating expense analysis by category.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear(){
        expenseComboBox.getSelectionModel().clearSelection();
        period1Start.setValue(null);
        period1End.setValue(null);
        period2Start.setValue(null);
        period2End.setValue(null);

        expenseComboBox.setDisable(false);
        period1Start.setDisable(false);
        period1End.setDisable(true);
        period2Start.setDisable(true);
        period2End.setDisable(true);

        totalExpenseButton.setDisable(true);
        compareExpenseButton.setDisable(true);
        numberOfEntriesButton.setDisable(true);
        categoryAnalysisButton.setDisable(true);
    }

    private String getSelectedCategory(){
        String category = expenseComboBox.getValue();
        if(category == null || category.isEmpty()){
            displayError("Please select a category");
            return null;
        }
        return category;
    }

    private Timestamp getTimestampFromDatePicker(DatePicker datePicker){
        if(datePicker.getValue() == null){
            displayError("PLease select a valid date.");
            return null;
        }
        return Timestamp.valueOf(datePicker.getValue().atStartOfDay());
    }

    private void displayReport(String content){
        reportDisplayPane.getChildren().clear();
        Text report = new Text(content);
        report.setWrappingWidth(reportDisplayPane.getWidth());
        reportDisplayPane.getChildren().add(report);
    }

    private void displayLineReport(Node content){
        reportDisplayPane.getChildren().clear();
        reportDisplayPane.getChildren().add(content);
        if(content instanceof Region) {
            Region region = (Region) content;

            region.setPrefWidth(reportDisplayPane.getWidth());
            region.setPrefHeight(reportDisplayPane.getHeight());

            region.prefWidthProperty().bind(reportDisplayPane.widthProperty());
            region.prefHeightProperty().bind(reportDisplayPane.heightProperty());
        }
    }

    private void displayError(String errorMessage){
        displayReport("Error: " + errorMessage);
    }

    private void displayExpensesInTable(List<Map<String, Object>> expenses, double totalExpense){
        reportDisplayPane.getChildren().clear();

        Map<String, Object> totalRow = new HashMap<>();
        totalRow.put("date", "");
        totalRow.put("category", "");
        totalRow.put("amount", totalExpense);
        totalRow.put("description", "Total");
        expenses.add(totalRow);

        TableView<Map<String, Object>> tableView = new TableView<>();

        TableColumn<Map<String, Object>, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get("date").toString()));

        TableColumn<Map<String, Object>, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Double)cellData.getValue().get("amount")));

        TableColumn<Map<String, Object>, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((String)cellData.getValue().get("description")));

        tableView.getColumns().addAll(dateColumn, amountColumn, descriptionColumn);

        tableView.getItems().addAll(expenses);

        tableView.setRowFactory(tv -> new TableRow<Map<String, Object>>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null && "Total".equals(item.get("description"))){
                    setStyle("-fx-background-color: lightblue; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox tableContainer = new VBox();
        tableContainer.setSpacing(10);
        tableContainer.setPadding(new Insets(10));
        tableContainer.getChildren().addAll(new Text("Expense Details: "), tableView);

        reportDisplayPane.getChildren().add(tableContainer);
    }

    private void displayComparisonTable(List<Map<String, Object>> combinedData) {

        reportDisplayPane.getChildren().clear();

        TableView<Map<String, Object>> tableView = new TableView<>();

        TableColumn<Map<String, Object>, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            Object date = cellData.getValue().get("date");
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });

        TableColumn<Map<String, Object>, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> {
            Object amount = cellData.getValue().get("amount");
            return new SimpleObjectProperty<>(amount != null ? (Double) amount : 0.0);
        });

        TableColumn<Map<String, Object>, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> {
            Object description = cellData.getValue().get("description");
            return new SimpleStringProperty(description != null ? description.toString() : "");
        });

        tableView.getColumns().addAll(dateColumn, amountColumn, descriptionColumn);

        tableView.getItems().addAll(combinedData);

        tableView.setRowFactory(tv ->
                new TableRow<Map<String, Object>>() {
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null) {
                            String description = (String) item.get("description");
                            if ("Difference".equals(description)) {
                                setStyle("-fx-background-color: lightblue; -fx-font-weight: bold;");
                            } else if (description != null && !description.contains("Total")) {
                                setStyle("-fx-font-weight: bold;");
                            } else {
                                setStyle("");
                            }
                        } else {
                            setStyle("");
                        }
                    }
                });

        tableView.setPrefWidth(reportDisplayPane.getWidth());
        tableView.setPrefHeight(reportDisplayPane.getHeight());

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox tableContainer = new VBox();
        tableContainer.setSpacing(10);
        tableContainer.setPadding(new Insets(10));
        tableContainer.getChildren().addAll(new Text("Comparison Details: "), tableView);
        reportDisplayPane.getChildren().add(tableContainer);
    }

    private void setupFieldListeners(){

        expenseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()){
                period1Start.setDisable(false);
            }
            updateButtonStates();
        });

        period1Start.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                period1End.setDisable(false);
                period1End.setDayCellFactory(getDateRestrictionsFactory(newValue, null));
            }
            updateButtonStates();
        });

        period1End.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                period2Start.setDisable(false);
            }
            updateButtonStates();
        });

        period2Start.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                period2End.setDisable(false);
                period2End.setDayCellFactory(getDateRestrictionsFactory(newValue, null));
            }
            updateButtonStates();
        });

        period2End.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateButtonStates();
        });
    }

    private Callback<DatePicker, DateCell> getDateRestrictionsFactory(LocalDate minDate, LocalDate maxDate) {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if((minDate != null && item.isBefore(minDate)) || (maxDate != null && item.isAfter(maxDate))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb");
                        }
                    }
                };
            }
        };
    }

    private void updateButtonStates(){
        boolean isExpenseSelected = expenseComboBox.getSelectionModel().getSelectedItem() != null;
        boolean isPeriod1StartSelected = period1Start.getValue() != null;
        boolean isPeriod1EndSelected = period1End.getValue() != null;
        boolean isPeriod2StartSelected = period2Start.getValue() != null;
        boolean isPeriod2EndSelected = period2End.getValue() != null;

        totalExpenseButton.setDisable(
                !(isExpenseSelected && isPeriod1StartSelected && isPeriod1EndSelected)
        );

        numberOfEntriesButton.setDisable(
                !(isExpenseSelected && isPeriod1StartSelected && isPeriod1EndSelected)
        );

        categoryAnalysisButton.setDisable(
                !(isPeriod1StartSelected && isPeriod1EndSelected)
        );

        compareExpenseButton.setDisable(
                !(isExpenseSelected && isPeriod1StartSelected && isPeriod1EndSelected && isPeriod2StartSelected && isPeriod2EndSelected)
        );
    }
}
