package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.compi.csc311group3.Expense;
import org.compi.csc311group3.database.ExpenseDAO;
import org.compi.csc311group3.model.Deposit;
import org.compi.csc311group3.service.DepositService;
import org.compi.csc311group3.service.ExpensesWithTotal;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;

import static org.compi.csc311group3.view.controllers.SettingsController.currencyController;

public class DashboardViewController implements Runnable{

    ExpenseDAO expenseDAO = new ExpenseDAO(); //used to access expense info for dashboard

    @FXML
    private Text totalBalanceText;
    @FXML
    private Text expensesText;
    @FXML
    private Text monthlyBudgetText;
    @FXML
    private Text savingsText;

    @FXML
    private Button dashboardLink;
    @FXML
    private Button analyticsLink;
    @FXML
    private Button addExpenseLink;
    @FXML
    private Button addDepositLink;
    @FXML
    private Button settingsLink;


    //bar chart code
    @FXML
    private BarChart<String, Number> barChart;
    private DepositService depositService;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private VBox recentExpensesContainer;


    public void initialize() throws SQLException, ClassNotFoundException {

        addRecentExpensesToDashboard(); //add recent expenses to the recentExpensesContainer

        //assign data from DB to these variables
        double balance = 3000;
        double expenses = 100;
        double monthlyBudget = 2000;
        double savings = 1000;

        String currentCurrency = currencyController.getCurrencyType();
        System.out.println("current currency: " + currentCurrency);
        System.out.println();

        ExpensesWithTotal expensesBundle = expenseDAO.getAllExpensesInAnObject();
        double totalExpenses = expensesBundle.getTotalExpenseAmount();
        Map dailyExpenses = expensesBundle.getDailyExpenses(); //contains users daily expenses for past week
        System.out.println("daily expenses" + dailyExpenses.toString());

        String balanceFormated = currencyController.convertCurrencyWithFormat(balance);
        String expensesFormated = currencyController.convertCurrencyWithFormat(totalExpenses);
        String monthlyBudgetFormated = currencyController.convertCurrencyWithFormat(monthlyBudget);
        String savingsFormatted = currencyController.convertCurrencyWithFormat(savings);


        expensesText.setStyle("-fx-fill: red"); //expenses should always be red
        monthlyBudgetText.setStyle("-fx-fill: #00be00"); //budget should always be green


        //sets text to assigned values
        //totalBalanceText.setText("$" + balance);
        //expensesText.setText("$" + expenses);
        //monthlyBudgetText.setText("$" + monthlyBudget);
        //savingsText.setText("$" + savings);
        totalBalanceText.setText(balanceFormated);
        expensesText.setText(expensesFormated);
        monthlyBudgetText.setText(monthlyBudgetFormated);
        savingsText.setText(savingsFormatted);


        /***** Bar chart code - start *****/

        /*barChart.legendVisibleProperty().setValue(false); //hides legend on bar chart

        yAxis.setLabel(currentCurrency); //sets the y-axis label to the current currency


        XYChart.Series set1 = new XYChart.Series<>();

        //assign data from DB to these variables
        double sundayExpense = 16.0;
        double mondayExpense = 5.0;
        double tuesdayExpense = 14.0;
        double wednesdayExpense = 9.0;
        double thursdayExpense = 12.0;
        double fridayExpense = 8.0;
        double saturdayExpense = 3.0;

        //data for each day of the week
        set1.getData().add(new XYChart.Data( "Sun", sundayExpense ));//new bar
        set1.getData().add(new XYChart.Data( "Mon", mondayExpense ));//new bar
        set1.getData().add(new XYChart.Data( "Tue", tuesdayExpense));//new bar
        set1.getData().add(new XYChart.Data( "Wed", wednesdayExpense));//new bar
        set1.getData().add(new XYChart.Data( "Thr", thursdayExpense));//new bar
        set1.getData().add(new XYChart.Data( "Fri", fridayExpense));//new bar
        set1.getData().add(new XYChart.Data( "Sat", saturdayExpense));//new bar


        barChart.getData().addAll(set1);//adds data to barChart


        /***** Bar chart code - end *****/

        depositService = new DepositService();
        calculateBalances();

        /***** Bar chart code - start *****/

        barChart.legendVisibleProperty().setValue(false);

        List<Expense> expensesList = expensesBundle.getExpenses(); //list of expenses
        yAxis.setLabel(currentCurrency); //set the y-axis to the current currency
        barChart.getData().clear(); //remove any existing data from the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(currentCurrency);

        //loop through the expenses and add each one to the chart
        for(Expense expense : expensesList) {
            String description = expense.getCategory(); //user expense category
            double amount = expense.getAmount();
            series.getData().add(new XYChart.Data<>(description, amount));
        }

        barChart.getData().add(series);

        /***** Bar chart code - end *****/

    }


    @FXML
    void dashboardLinkClicked(ActionEvent event) {

    }
    @FXML
    void analyticsLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("Analytics.fxml", 800, 600, analyticsLink);
    }
    @FXML
    void addExpenseLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("Expense.fxml", 850, 560, addExpenseLink);
    }
    @FXML
    void addDepositLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("deposit-view.fxml", 850, 560, addDepositLink);
    }
    @FXML
    void settingsLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("settings.fxml", 850, 560, settingsLink);
    }


    //overrides the run method from the Runnable interface
    @Override
    public void run() {
        exportBarChartToPDF(); //calls method to export bar chart
    }

    //exports bar chart
    public void exportBarChartToPDF() {
        Stage stage1 = getStage(this.barChart); //stage where bar chart it located

        PrinterJob printerJob = PrinterJob.createPrinterJob(); //create a printer job

        if (printerJob != null && printerJob.showPrintDialog(stage1)) { //

            boolean success = printerJob.printPage(barChart); //prints the chart

            if (success) { //runs if export is successfully
                printerJob.endJob(); //closes the printer job
                System.out.println("export successful");
            } else {
                System.out.println("export failed");
            }
        } else {
            System.out.println("chart could not be exported");
        }
    } //end exportBarChartToPDF


    //finds the stage where barChart is located
    public static Stage getStage(BarChart<?, ?> barChart) {
        if (barChart.getScene() != null && barChart.getScene().getWindow() instanceof Stage) {
            return (Stage) barChart.getScene().getWindow();
        }
        return null; //returns null if the bar chart is not on a stage
    }

    //method that creates and starts the thread
    public void startExport(){
        Thread myThread = new Thread(this);
        myThread.start();
    }

    /*public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) totalBalanceText.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/org/compi/csc311group3/styling/theme4.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * This method is used to calculate the total balance and savings and display them on the dashboard
     * TODO: Incorporate the expenses into the calculation
     * //the above todo is now complete
     */
    private void calculateBalances() throws SQLException, ClassNotFoundException {
        List<Deposit> deposits = depositService.getDeposits();
        double savings = 0;
        double checking = 0;
        for (Deposit deposit : deposits) {
            if (deposit.getAccount().equals("savings")) {
                savings += deposit.getAmount();
            } else {
                checking += deposit.getAmount();
            }
        }

        ExpensesWithTotal expenseBundle = expenseDAO.getAllExpensesInAnObject(); //holds information about all users expenses
        double totalExpenses = expenseBundle.getTotalExpenseAmount(); //total expenses calculated
        double totalBalance = (savings + checking) - totalExpenses;

        //change color of text based on positive or negative
        if(totalBalance > 0){
            totalBalanceText.setStyle("-fx-fill: #00be00");
        }
        if(totalBalance < 0){
            totalBalanceText.setStyle("-fx-fill: red");
        }

        if(savings > 0){
            savingsText.setStyle("-fx-fill: #00be00");
        }
        if(savings < 0){
            savingsText.setStyle("-fx-fill: red");
        }





        totalBalanceText.setText(currencyController.convertCurrencyWithFormat(totalBalance)); //display balance with proper currency unit
        savingsText.setText(currencyController.convertCurrencyWithFormat(savings)); //display savings with proper currency unit
    }


    private void addRecentExpensesToDashboard() throws SQLException, ClassNotFoundException {

        ExpensesWithTotal expenseBundle = expenseDAO.getAllExpensesInAnObject();
        List<Expense> expenseList = expenseBundle.getExpenses(); //gets list of all expenses

        recentExpensesContainer.getChildren().clear(); //clear container before adding new things

        recentExpensesContainer.setSpacing(5); //spacing between items in the container


        expenseList.sort(Comparator.comparing(Expense::getDate_time).reversed()); //sort expenseList based on date

        //loop through the top 5 recent expenses or fewer if there are less than 5
        int count = Math.min(6, expenseList.size());  //ensure we don't exceed the list size
        for (int i = 0; i < count; i++) {
            Expense expense = expenseList.get(i);

            //create a VBox for each expense
            VBox expenseVBox = new VBox(1); // 1 is the spacing between elements inside the VBox

            //dateTimeFormatter to format the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            //create labels to display expense details
            Label descriptionLabel = new Label(String.valueOf(expense.getDescription()));
            Label dateLabel = new Label(expense.getDate_time().toLocalDate().format(formatter)); //date formatted as MM/dd/yyyy
            Label amountLabel = new Label(currencyController.convertCurrencyWithFormat(expense.getAmount()));

            //styles for labels
            descriptionLabel.setStyle("-fx-font-weight: bold;");
            amountLabel.setStyle("-fx-text-fill: red;");

            //add the labels to the VBox
            expenseVBox.getChildren().addAll(dateLabel, descriptionLabel, amountLabel); //order matters

            //add some style for the expenseVBox
            expenseVBox.setStyle("-fx-background-color: transparent; -fx-padding: 5 0 5 0; -fx-border-color: transparent transparent #c5c5c5 transparent; -fx-border-radius: 0px; -fx-background-radius: 0px");
            //add the VBox to the container
            recentExpensesContainer.getChildren().add(expenseVBox);
        }

    }

}
