package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.compi.csc311group3.service.UserService;

import java.io.IOException;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;

public class RegistrationController {
    @FXML
    private TextField regUsername;
    @FXML
    private TextField regPassword;
    @FXML
    private TextField regEmail;
    @FXML
    private Button registerbttn;

    private UserService userService = UserService.getInstance();

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
    private void Register(ActionEvent event) throws IOException {
        if(isUsernameValid() && isPasswordValid() && isEmailValid()) {
            userService.SignUp(regEmail.getText(), regUsername.getText(), regPassword.getText());
        }
        ChangeScreen("login-view.fxml", 600, 400, registerbttn);
    }
}
