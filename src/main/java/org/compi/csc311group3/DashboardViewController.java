package org.compi.csc311group3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;



public class DashboardViewController implements Runnable{


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





}
