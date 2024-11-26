package org.compi.csc311group3;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import jdk.jfr.Category;
import org.compi.csc311group3.service.UserService;
import org.compi.csc311group3.view.controllers.LoginController;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;


public class ExpenseController {

    public Text dashboardLink;
    public Text analyticsLink;
    public Text addExpenseLink;
    public Text addDepositLink;
    public Text settingsLink;
    @FXML
    private TableView<Expense> expenseTableView;

    @FXML
    private TableColumn<Expense, String> descriptionColumn;

    @FXML
    private TableColumn<Expense, String> categoryColumn;

    @FXML
    private TableColumn<Expense, LocalDateTime> dateTimeColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker dateTimeField;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button newCategoryButton;
    

    private ObservableList<Expense> expenses;
    private ObservableList<String> categories;
    private UserService userService = UserService.getInstance();

    public void initialize() {
        expenses = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList("Food", "Rent", "Utilities", "Entertainment");
        expenseTableView.setItems(expenses);
        categoryComboBox.setItems(categories);
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        addButton.setOnAction(e -> addExpense());
        editButton.setOnAction(e -> editExpense());
        deleteButton.setOnAction(e -> deleteExpense());
        newCategoryButton.setOnAction(e -> createNewCategory());
    }

    private void addExpense() {
        String description = descriptionField.getText();
        String category = categoryComboBox.getValue();
        double amount = Double.parseDouble(amountField.getText());
        LocalDateTime dateTime = dateTimeField.getValue().atStartOfDay();
        Expense expense = new Expense(description, category, dateTime, amount);
        expenses.add(expense);
        userService.addExpense(expense.getAmount(),expense.getCategory(),expense.getDescription());
        userService.printAllExpenses();
        clearFields();
    }

    public void editExpense(){
        Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
        if(selectedExpense != null){
           String description = descriptionField.getText();
           String category = categoryComboBox.getValue();
           String amountText = amountField.getText();
           LocalDateTime dateTime = dateTimeField.getValue().atStartOfDay();

           if(description.isEmpty() || category == null || amountText.isEmpty() || dateTime == null ){
               showErrorAlert("Please fill in all fields before editing");
               return;
           }
           try {
               double amount = Double.parseDouble(amountText);
               selectedExpense.setDescription(description);
               selectedExpense.setCategory(category);
               selectedExpense.setAmount(amount);
               selectedExpense.setDateTime(dateTime);

               expenseTableView.refresh();
               clearFields();
           } catch (NumberFormatException e) {
               showErrorAlert("Please enter a valid amount");
           }
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void deleteExpense(){
        Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
        if(selectedExpense != null){
            expenses.remove(selectedExpense);
        }
    }

    private void createNewCategory(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New category");
        dialog.setHeaderText("Add a new category");
        dialog.setContentText("Enter the name of the new category");
        dialog.showAndWait().ifPresent(category -> {
            if(!categories.contains(category)){
                categories.add(category);
            }
        });
    }

    private void clearFields(){
        descriptionField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        amountField.clear();
        dateTimeField.setValue(null);
    }
    @FXML
    void dashboardLinkClicked(javafx.scene.input.MouseEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, addExpenseLink);
    }
    @FXML
    void analyticsLinkClicked(javafx.scene.input.MouseEvent event) {

        // TODO: Implement functionality to navigate to analytics page

    }
    @FXML
    void addExpenseLinkClicked(javafx.scene.input.MouseEvent event) throws IOException {
        ChangeScreen("Expense.fxml", 850, 560, addExpenseLink);
    }
    @FXML
    void addDepositLinkClicked(javafx.scene.input.MouseEvent event) {

        // TODO: Implement functionality to navigate to addDeposit page

    }
    @FXML
    void settingsLinkClicked(javafx.scene.input.MouseEvent event) {

        // TODO: Implement functionality to navigate to settings page
    }

}
