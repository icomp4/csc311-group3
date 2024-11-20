package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField regUsername;
    @FXML
    private TextField regPassword;
    @FXML
    private TextField regEmail;
    @FXML
    private Button registerbttn;

    public void initialize() {
        // Disable the login button, and attach our validation methods
        registerbttn.setDisable(true);

        // Add listeners to trigger validation
        regUsername.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        regPassword.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        regEmail.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
    }

    private void validateInputs() {
        boolean isUsernameValid = isUsernameValid();
        boolean isPasswordValid = isPasswordValid();
        boolean isEmailValid = isEmailValid();

        // Enable login button only if all fields are valid
        registerbttn.setDisable(!(isUsernameValid && isPasswordValid && isEmailValid));
    }

    private boolean isEmailValid() {
        String email = regEmail.getText();
        return email != null && !email.trim().isEmpty() && email.contains("@") && email.contains(".");
    }

    private boolean isUsernameValid() {
        String username = regUsername.getText();
        return username != null && !username.trim().isEmpty() && username.length() >= 5;
    }

    /**
     * Validates the password field
     * @return true if the password is not empty and has at least 5 characters
     */
    private boolean isPasswordValid() {
        String password = regPassword.getText();

        return password != null && !password.trim().isEmpty() && password.length() >= 5;
    }


    //TODO Registration CSS page
    @FXML
    private void Register(ActionEvent event) {
        //TODO adds it to the database

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/compi/csc311group3/login-view.fxml"));
            Scene registrationScene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();//dont rly understand this line
            stage.setScene(registrationScene);
            stage.setTitle("App Name");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
