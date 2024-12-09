package org.compi.csc311group3.view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private Pane theme1Pane;
    @FXML
    private Pane theme2Pane;
    @FXML
    private Pane theme3Pane;
    @FXML
    private Pane theme4Pane;


    @FXML
    private AnchorPane root; //used to locate Pane


    //bar chart code
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ComboBox<String> currencyMenu;

    public static CurrencyController currencyController = new CurrencyController();


    public void initialize() {

        setThemeCircleIndicator();

        ObservableList<String> currencies = FXCollections.observableArrayList(
                "USD", "EUR", "JPY", "GBP", "CHF"
        );

        currencyMenu.setItems(currencies);
        currencyMenu.setValue(currencyController.getCurrencyType());  //default value

        currencyMenu.setOnAction(event -> {
            String selectedCurrency = currencyMenu.getValue(); //get the selected currency from combobox
            System.out.println("selected currency: " + selectedCurrency);
            currencyController.setCurrencyType(selectedCurrency);//change currency type in the currencyController
        });


    }

    public void clearThemeCircles()
    {
        theme1Pane.setStyle("-fx-border-color: transparent");
        theme2Pane.setStyle("-fx-border-color: transparent");
        theme3Pane.setStyle("-fx-border-color: transparent");
        theme4Pane.setStyle("-fx-border-color: transparent");
    }

    private void setThemeCircleIndicator(){
        ThemeController.Theme theme = ThemeController.getCurrentTheme();
        if(theme .equals(ThemeController.Theme.THEME_ONE)){
            clearThemeCircles();
            theme1Pane.setStyle("-fx-border-color: #FF6347;" +
                    "-fx-border-width: 3px;"
                    + "-fx-border-radius: 100px;"
                    + "-fx-pref-width: 70px;"
                    + "-fx-pref-height: 70px;");
        } else if(theme .equals(ThemeController.Theme.THEME_TWO)){
            clearThemeCircles();
            theme2Pane.setStyle("-fx-border-color: #FF6347;" +
                    "-fx-border-width: 3px;"
                    + "-fx-border-radius: 100px;"
                    + "-fx-pref-width: 70px;"
                    + "-fx-pref-height: 70px;");
        } else if (theme .equals(ThemeController.Theme.THEME_THREE)){
            clearThemeCircles();
            theme3Pane.setStyle("-fx-border-color: #FF6347;" +
                    "-fx-border-width: 3px;"
                    + "-fx-border-radius: 100px;"
                    + "-fx-pref-width: 70px;"
                    + "-fx-pref-height: 70px;");
        } else if (theme .equals(ThemeController.Theme.THEME_FOUR)){
            clearThemeCircles();
            theme4Pane.setStyle("-fx-border-color: #FF6347;" +
                    "-fx-border-width: 3px;"
                    + "-fx-border-radius: 100px;"
                    + "-fx-pref-width: 70px;"
                    + "-fx-pref-height: 70px;");
        }
    }


    @FXML
    void dashboardLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, dashboardLink);
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
        setThemeCircleIndicator();
    }
    public void setTheme2()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_TWO);
        setThemeCircleIndicator();
    }
    public void setTheme3()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_THREE);
        setThemeCircleIndicator();
    }
    public void setTheme4()
    {
        ThemeController.applyTheme(ThemeController.Theme.THEME_FOUR);
        setThemeCircleIndicator();
    }


}
