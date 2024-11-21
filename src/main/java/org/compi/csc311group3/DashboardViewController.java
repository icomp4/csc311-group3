package org.compi.csc311group3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class DashboardViewController {


    @FXML
    private Text dashboardLink;
    @FXML
    private Text analyticsLink;
    @FXML
    private Text addExpenseLink;
    @FXML
    private Text addDepositLink;
    @FXML
    private Text settingsLink;


    //bar chart code
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;



    public void initialize() {


        barChart.legendVisibleProperty().setValue(false);

        XYChart.Series set1 = new XYChart.Series<>();

        //assign data from DB to these variables
        double sundayExpense = 16.0;
        double mondayExpense = 5.0;
        double tuesdayExpense = 14.0;
        double wednesdayExpense = 9.0;
        double thursdayExpense = 12.0;
        double fridayExpense = 8.0;
        double saturdayExpense = 3.0;

        set1.getData().add(new XYChart.Data( "Sun", sundayExpense ));
        set1.getData().add(new XYChart.Data( "Mon", mondayExpense ));
        set1.getData().add(new XYChart.Data( "Tue", tuesdayExpense));
        set1.getData().add(new XYChart.Data( "Wed", wednesdayExpense));
        set1.getData().add(new XYChart.Data( "Thr", thursdayExpense));
        set1.getData().add(new XYChart.Data( "Fri", fridayExpense));
        set1.getData().add(new XYChart.Data( "Sat", saturdayExpense));




        barChart.getData().addAll(set1);
    }


    @FXML
    void dashboardLinkClicked(ActionEvent event) {

        // TODO: Implement dashboard functionality to navigate to dashboard page

    }
    @FXML
    void analyticsLinkClicked(ActionEvent event) {

        // TODO: Implement functionality to navigate to analytics page

    }
    @FXML
    void addExpenseLinkClicked(ActionEvent event) {

        // TODO: Implement functionality to navigate to addExpense page

    }
    @FXML
    void addDepositLinkClicked(ActionEvent event) {

        // TODO: Implement functionality to navigate to addDeposit page

    }
    @FXML
    void settingsLinkClicked(ActionEvent event) {

        // TODO: Implement functionality to navigate to settings page
    }





}
