package org.compi.csc311group3.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class AnalyticsController {

    @FXML
    private DatePicker period1Start, period1End, period2Start, period2End;

    @FXML
    private ComboBox<String> categoryComboBox;
}
