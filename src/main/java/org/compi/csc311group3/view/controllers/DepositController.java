package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import org.compi.csc311group3.model.Deposit;
import org.compi.csc311group3.service.DepositService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;

public class DepositController {
    @FXML private ChoiceBox<String> accountSelector;
    @FXML private Button addDepositLink;
    @FXML private Button addExpenseLink;
    @FXML private TextField amountField;
    @FXML private Button analyticsLink;
    @FXML private Button dashboardLink;
    @FXML private VBox leftSection;
    @FXML private Text logoText;
    @FXML private Pane midSection;
    @FXML private Button settingsLink;
    @FXML private ScrollPane scrollPane;

    private HBox depositCardsContainer;
    private List<DepositRecord> depositHistory;
    private DepositService depositService;

    public void initialize() {
        accountSelector.getItems().addAll("Checking", "Savings");
        depositHistory = new ArrayList<>();

        depositCardsContainer = new HBox(10);
        depositCardsContainer.setPadding(new Insets(10));

        scrollPane.setContent(depositCardsContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        depositService = new DepositService();

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        depositCardsContainer.getChildren().add(loadingIndicator);

        Thread depositLoader = new Thread(() -> {
            List<Deposit> deposits = depositService.getDeposits();

            javafx.application.Platform.runLater(() -> {
                depositCardsContainer.getChildren().clear();

                for (Deposit deposit : deposits) {
                    DepositRecord record = new DepositRecord(
                            LocalDate.now(),
                            deposit.getAmount(),
                            deposit.getAccount()
                    );
                    depositHistory.add(record);
                    Pane card = createDepositCard(record);
                    depositCardsContainer.getChildren().add(card);
                }
            });
        });

        depositLoader.setDaemon(true);
        depositLoader.start();
    }

    @FXML
    void addDeposit(ActionEvent event) {
        try {
            if (accountSelector.getValue() == null || amountField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            double amount = Double.parseDouble(amountField.getText());

            DepositRecord newDeposit = new DepositRecord(
                    LocalDate.now(),
                    amount,
                    accountSelector.getValue()
            );

            depositHistory.add(0, newDeposit);
            if(accountSelector.getValue().equals("Checking")) {
                depositService.addDeposit("checking", amount);
            } else {
                depositService.addDeposit("savings", amount);
            }
            Pane newCard = createDepositCard(newDeposit);
            depositCardsContainer.getChildren().add(0, newCard);

            amountField.clear();
            accountSelector.setValue(null);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    private Pane createDepositCard(DepositRecord deposit) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-radius: 5; " +
                "-fx-background-radius: 5; -fx-border-color: #e0e0e0;");
        card.setPrefWidth(200);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        String dateStr = deposit.date.format(formatter);

        Text titleText = new Text("Deposit " + dateStr);
        Text amountText = new Text(String.format("$%.2f", deposit.amount));
        amountText.setFill(Color.GREEN);
        Text accountText = new Text(deposit.accountType);

        card.getChildren().addAll(titleText, amountText, accountText);

        return card;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addExistingData() {
        List<Deposit> deposits = depositService.getDeposits();
        for (Deposit deposit : deposits) {
            DepositRecord record = new DepositRecord(
                    LocalDate.now(),
                    deposit.getAmount(),
                    deposit.getAccount()
            );
            depositHistory.add(record);
            Pane card = createDepositCard(record);
            depositCardsContainer.getChildren().add(card);
        }
    }

    private static class DepositRecord {
        LocalDate date;
        double amount;
        String accountType;

        DepositRecord(LocalDate date, double amount, String accountType) {
            this.date = date;
            this.amount = amount;
            this.accountType = accountType;
        }
    }

    @FXML void addDepositLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("deposit-view.fxml", 850, 560, addDepositLink);
    }
    @FXML void addExpenseLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("Expense.fxml", 850, 560, addExpenseLink);
    }
    @FXML void analyticsLinkClicked(ActionEvent event) { }
    @FXML void dashboardLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, dashboardLink);
    }
    @FXML
    void settingsLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("settings.fxml", 850, 560, settingsLink);
    }
}