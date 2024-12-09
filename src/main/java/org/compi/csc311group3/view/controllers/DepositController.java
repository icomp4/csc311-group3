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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;

import static org.compi.csc311group3.view.controllers.SettingsController.currencyController;//import currencyController instance from settings

/**
 * The DepositController class is responsible for managing the UI for the deposit screen
 * It features logic for handling user input regarding creating deposits, as well as pulling deposit history from the database
 */
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
    @FXML private Label savingsMonth;
    @FXML private Label savingsWeek;
    @FXML private Label savingsYear;
    @FXML private Label checkingMonth;
    @FXML private Label checkingWeek;
    @FXML private Label checkingYear;

    private HBox depositCardsContainer;
    private List<DepositRecord> depositHistory;
    private DepositService depositService;

    /**
     * The initialize function is called on page load
     * it is responsible for initializing the scrollpane, choicebox, and spawning threads to fetch data from the db
     */
    public void initialize() {
        depositCardsContainer = new HBox(10);
        depositCardsContainer.setPadding(new Insets(10));
        depositService = new DepositService();
        depositHistory = new ArrayList<>();

        scrollPane.setContent(depositCardsContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);

        accountSelector.setValue("Choose Account");
        accountSelector.getItems().addAll("Checking", "Savings");

        ProgressIndicator cardsLoadingIndicator = new ProgressIndicator();
        depositCardsContainer.getChildren().add(cardsLoadingIndicator);

        checkingWeek.setText("Loading...");
        checkingMonth.setText("Loading...");
        checkingYear.setText("Loading...");
        savingsWeek.setText("Loading...");
        savingsMonth.setText("Loading...");
        savingsYear.setText("Loading...");

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

        Thread statsLoader = new Thread(() -> {
            updateDepositStats();
        });

        depositLoader.setDaemon(true);
        statsLoader.setDaemon(true);

        depositLoader.start();
        statsLoader.start();
    }

    /**
     * Updates the deposit scrollPane, then attempts to add the deposit to the database
     */
    @FXML
    void addDeposit(ActionEvent event) {
        try {
            if (accountSelector.getValue() == null || amountField.getText().isEmpty() || accountSelector.getValue().equals("Choose Account")) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            double amountInUserCurrency = Double.parseDouble(amountField.getText()); //amount before converted to USD
            double amount = currencyController.convertToUSD(amountInUserCurrency); //converted to USD before storing in database

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

            updateDepositStats();
            amountField.clear();
            accountSelector.setValue(null);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    /**
     * Takes a deposit and models it as a card to be displayed on the UI
     * @param deposit the deposit data
     * @return the card with the deposit info
     */
    private Pane createDepositCard(DepositRecord deposit) {
        VBox card = new VBox(5);
        card.getStyleClass().add("deposit-card"); //added class for styling
        card.setPadding(new Insets(10));
        //commented out background color so that it would not overwrite the dark theme color
        card.setStyle("/*-fx-background-color: white;*/ -fx-border-radius: 5; " +
                "-fx-background-radius: 5; /*-fx-border-color: #e0e0e0;*/");
        card.setPrefWidth(200);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        String dateStr = deposit.date.format(formatter);

        Text titleText = new Text("Deposit " + dateStr);
        String convertedAmount = currencyController.convertCurrencyWithFormat(deposit.amount); //converts amount without formatting since formatting gets applied in next line
        //Text amountText = new Text(String.format("$%.2f", convertedAmount)); //second parameter is the converted amount.  the amount in the currency that the user selected in settings
        Text amountText = new Text(convertedAmount); //text with currency symbol and value. ex $150 or €150
        //removed green fill color for amountText. I instead added class to be styles in stylesheet
        amountText.getStyleClass().add("amount-text"); //added class for styling
        Text accountText = new Text(deposit.accountType);
        accountText.getStyleClass().add("account-text");//added class for styling

        card.getChildren().addAll(titleText, amountText, accountText);

        return card;
    }

    /**
     * Displays an error alert
     * @param title the title of the alert
     * @param content the content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Helper data class to model a deposit
     */
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

    /**
     * This method fetches deposit data from the database, and updates the Deposit Statistic UI fields
     * It categorizes deposits based on account, and timeframe
     * It then sums them based on the criteria, and updates the label for the correlated field
     */
    private void updateDepositStats() {
        LocalDate now = LocalDate.now();
        LocalDate weekAgo = now.minusWeeks(1);
        LocalDate monthAgo = now.minusMonths(1);
        LocalDate yearAgo = now.minusYears(1);

        List<Deposit> deposits = depositService.getDeposits();

        BigDecimal checkingWeekTotal = BigDecimal.ZERO;
        BigDecimal checkingMonthTotal = BigDecimal.ZERO;
        BigDecimal checkingYearTotal = BigDecimal.ZERO;
        BigDecimal savingsWeekTotal = BigDecimal.ZERO;
        BigDecimal savingsMonthTotal = BigDecimal.ZERO;
        BigDecimal savingsYearTotal = BigDecimal.ZERO;

        for (Deposit deposit : deposits) {
            LocalDate depositDate = deposit.getDateTime().toLocalDateTime().toLocalDate();
            BigDecimal amount = BigDecimal.valueOf(deposit.getAmount());

            if (deposit.getAccount().equalsIgnoreCase("checking")) {
                if (depositDate.isAfter(weekAgo)) {
                    checkingWeekTotal = checkingWeekTotal.add(amount);
                }
                if (depositDate.isAfter(monthAgo)) {
                    checkingMonthTotal = checkingMonthTotal.add(amount);
                }
                if (depositDate.isAfter(yearAgo)) {
                    checkingYearTotal = checkingYearTotal.add(amount);
                }
            } else if (deposit.getAccount().equalsIgnoreCase("savings")) {
                if (depositDate.isAfter(weekAgo)) {
                    savingsWeekTotal = savingsWeekTotal.add(amount);
                }
                if (depositDate.isAfter(monthAgo)) {
                    savingsMonthTotal = savingsMonthTotal.add(amount);
                }
                if (depositDate.isAfter(yearAgo)) {
                    savingsYearTotal = savingsYearTotal.add(amount);
                }
            }
        }

        BigDecimal finalCheckingWeekTotal = checkingWeekTotal;
        BigDecimal finalCheckingMonthTotal = checkingMonthTotal;
        BigDecimal finalCheckingYearTotal = checkingYearTotal;
        BigDecimal finalSavingsWeekTotal = savingsWeekTotal;
        BigDecimal finalSavingsMonthTotal = savingsMonthTotal;
        BigDecimal finalSavingsYearTotal = savingsYearTotal;
        javafx.application.Platform.runLater(() -> {
            checkingWeek.setText(currencyController.convertCurrencyWithFormat(finalCheckingWeekTotal.doubleValue()));
            checkingMonth.setText(currencyController.convertCurrencyWithFormat(finalCheckingMonthTotal.doubleValue()));
            checkingYear.setText(currencyController.convertCurrencyWithFormat(finalCheckingYearTotal.doubleValue()));

            savingsWeek.setText(currencyController.convertCurrencyWithFormat(finalSavingsWeekTotal.doubleValue()));
            savingsMonth.setText(currencyController.convertCurrencyWithFormat(finalSavingsMonthTotal.doubleValue()));
            savingsYear.setText(currencyController.convertCurrencyWithFormat(finalSavingsYearTotal.doubleValue()));
        });
    }

    // Navigation links

    @FXML void addDepositLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("deposit-view.fxml", 850, 560, addDepositLink);
    }
    @FXML void addExpenseLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("Expense.fxml", 850, 560, addExpenseLink);
    }
    @FXML void analyticsLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("Analytics.fxml", 800, 600, analyticsLink);
    }
    @FXML void dashboardLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("dashboard-view.fxml", 850, 560, dashboardLink);
    }
    @FXML
    void settingsLinkClicked(ActionEvent event) throws IOException {
        ChangeScreen("settings.fxml", 850, 560, settingsLink);
    }
}