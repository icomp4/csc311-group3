package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

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

    public void initialize() {
        accountSelector.getItems().addAll("Checking", "Savings");

        depositHistory = new ArrayList<>();

        depositCardsContainer = new HBox(10);
        depositCardsContainer.setPadding(new Insets(10));

        scrollPane.setContent(depositCardsContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);

        addSampleData();
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

    private void addSampleData() {

        // Sample data
        depositHistory.add(new DepositRecord(LocalDate.now().minusDays(5), 1200.04, "Savings"));
        depositHistory.add(new DepositRecord(LocalDate.now().minusDays(20), 154.95, "Checking"));
        depositHistory.add(new DepositRecord(LocalDate.now().minusDays(35), 249.47, "Checking"));
        depositHistory.add(new DepositRecord(LocalDate.now().minusDays(50), 2895.45, "Savings"));

        for (DepositRecord deposit : depositHistory) {
            depositCardsContainer.getChildren().add(createDepositCard(deposit));
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

    @FXML void addDepositLinkClicked(ActionEvent event) throws IOException { }
    @FXML void addExpenseLinkClicked(ActionEvent event) { }
    @FXML void analyticsLinkClicked(ActionEvent event) { }
    @FXML void dashboardLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, dashboardLink);
    }
    @FXML void settingsLinkClicked(ActionEvent event) { }
}