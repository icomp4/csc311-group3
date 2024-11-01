package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField pwTextField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTextField;

    public void initialize() {

        // Set the ids for the elements so we can style them individually in the CSS file
        loginButton.setId("loginButton");
        pwTextField.setId("pwTextField");
        usernameTextField.setId("usernameTextField");
        registerButton.setId("registerButton");
    }
    @FXML
    void LoginClicked(ActionEvent event) {

        // TODO: Implement login functionality after implementing the database

    }

    @FXML
    void RegisterClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/compi/csc311group3/registration-view.fxml"));
            Scene registrationScene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();//dont rly understand this line
            stage.setScene(registrationScene);
            stage.setTitle("Registration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Implement opening the registration page once it's added

}
