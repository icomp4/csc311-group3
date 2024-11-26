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

import static org.compi.csc311group3.HelloApplication.ChangeScreen;
import org.compi.csc311group3.database.DbConnection;
import org.compi.csc311group3.database.ExpenseDAO;
import org.compi.csc311group3.service.UserService;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private TableColumn<Expense, Integer> idColumn;
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
    

    private UserService userService = UserService.getInstance();

    private final DbConnection dbConnection = new DbConnection();
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    public void initialize() throws SQLException, ClassNotFoundException {
        expenseDAO.initialize();
        loadExpenses();

        List<String> categoryList = expenseDAO.getUniqueCategories();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryList));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("date_time"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        addButton.setOnAction(e -> addExpense());
        editButton.setOnAction(e -> editExpense());
        deleteButton.setOnAction(e -> deleteExpense());
        newCategoryButton.setOnAction(e -> createNewCategory());
    }


    public void addExpense() {
        LocalDateTime date_time = dateTimeField.getValue() != null ? dateTimeField.getValue().atStartOfDay() : null;
        String description = descriptionField.getText();
        String category = categoryComboBox.getValue();
        String amountText = amountField.getText();

        if(description.isEmpty() || category == null || amountText.isEmpty() || date_time == null){
        showErrorAlert("Please fill in all fields");
        return;
        }

        double amount;
        try{
            amount = Double.parseDouble(amountText);
        } catch(NumberFormatException e){
            showErrorAlert("Please enter a valid amount");
            return;
        }

        Expense expense = new Expense(date_time, description, category, amount);
        try{
            int generateId = expenseDAO.addExpense(expense);
            loadExpenses();
            clearFields();
        } catch (SQLException | ClassNotFoundException e){
            showErrorAlert("Error adding expense: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editExpense(){
        Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
        if(selectedExpense != null){
           String description = descriptionField.getText();
           String category = categoryComboBox.getValue();
           String amountText = amountField.getText();
           LocalDateTime date_time = dateTimeField.getValue().atStartOfDay();

           if(description.isEmpty() || category == null || amountText.isEmpty() || date_time == null ){
               showErrorAlert("Please fill in all fields before editing");
               return;
           }
           double amount;
           try{
               amount = Double.parseDouble(amountText);
           } catch (NumberFormatException e){
                   showErrorAlert("Please enter a valid amount");
                   return;
           }
           selectedExpense.setDate_time(date_time);
           selectedExpense.setDescription(description);
           selectedExpense.setCategory(category);
           selectedExpense.setAmount(amount);

           try {
               expenseDAO.updateExpense(selectedExpense);
               loadExpenses();
               clearFields();
           } catch (SQLException | ClassNotFoundException e){
               showErrorAlert("Error editing expense: " + e.getMessage());
               e.printStackTrace();
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
            try{
                expenseDAO.deleteExpense(selectedExpense.getId());
                loadExpenses();
            } catch (SQLException | ClassNotFoundException e){
                showErrorAlert("Error deleting expense: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void createNewCategory(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New category");
        dialog.setHeaderText("Add a new category");
        dialog.setContentText("Enter the name of the new category");
        dialog.showAndWait().ifPresent(category -> {
            try{
            if(!expenseDAO.categoryExists(category)){
                expenseDAO.addCategory(category);
                List<String> categoryList = expenseDAO.getUniqueCategories();
                categoryComboBox.setItems(FXCollections.observableArrayList(categoryList));
            } else {
                showErrorAlert("Category already exists");
                }
            } catch (SQLException | ClassNotFoundException e) {
                    showErrorAlert("Error adding category: " + e.getMessage());
                    e.printStackTrace();
                }
        });
    }

    private void clearFields(){
        descriptionField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        amountField.clear();
        dateTimeField.setValue(null);
    }

    private void loadExpenses(){
        try{
            List<Expense> expenseList = expenseDAO.getAllExpenses();
            expenseTableView.setItems(FXCollections.observableArrayList(expenseList));
        } catch (SQLException | ClassNotFoundException e){
            showErrorAlert("Error loading expenses: " + e.getMessage());
            e.printStackTrace();
        }
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
