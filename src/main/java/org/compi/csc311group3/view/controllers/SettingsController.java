package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


import static org.compi.csc311group3.HelloApplication.ChangeScreen;


public class SettingsController {

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

    @FXML
    private AnchorPane root; //used to locate Pane


    //bar chart code
    @FXML
    private BarChart<String, Number> barChart;



    public void initialize() {



    }


    @FXML
    void dashboardLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, dashboardLink);
    }
    @FXML
    void analyticsLinkClicked(ActionEvent event) {

        // TODO: Implement functionality to navigate to analytics page

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


    /*public void theme4(MouseEvent mouseEvent) {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            URL cssURL = getClass().getResource("/org/compi/csc311group3/styling/theme4.css");
            System.out.println("css file location: " + cssURL.toExternalForm());
            scene.getStylesheets().add(cssURL.toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void setTheme1(MouseEvent mouseEvent)
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_ONE);
    }
    public void setTheme2()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_TWO);
    }
    public void setTheme3()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_THREE);
    }
    public void setTheme4()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_FOUR);
    }


}
