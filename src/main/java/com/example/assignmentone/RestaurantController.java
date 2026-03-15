package com.example.assignmentone;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RestaurantController {
    @FXML private Label timeLabel;
    @FXML private ListView<String> orderListView;
    @FXML private Label totalLabel;
    @FXML private TextField cashField;
    @FXML private Label changeLabel;
    @FXML private Button clearOrderButton;
    @FXML private Button calculateButton;
    @FXML private Button completeButton;

    @FXML
    public void initialize() {
        updateTime();
        if (totalLabel != null) {
            totalLabel.setText("M0.00");
        }
        if (changeLabel != null) {
            changeLabel.setText("M0.00");
        }
        startTimeUpdate();

        // CONNECT BUTTONS PROGRAMMATICALLY (REMOVE onAction from FXML)
        if (clearOrderButton != null) {
            clearOrderButton.setOnAction(_ -> handleClearOrder());
        }
        if (calculateButton != null) {
            calculateButton.setOnAction(_ -> handleCalculateChange());
        }
        if (completeButton != null) {
            completeButton.setOnAction(_ -> handleCompleteOrder());
        }
    }

    private void updateTime() {
        if (timeLabel != null) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            timeLabel.setText("🕐 " + time);
        }
    }

    private void startTimeUpdate() {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), _ -> updateTime())
        );
        timeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timeline.play();
    }

    // REMOVE @FXML FROM THESE METHODS
    private void handleCalculateChange() {
        try {
            double cash = Double.parseDouble(cashField.getText());
            double total = Double.parseDouble(totalLabel.getText().replace("M", "").trim());
            double change = cash - total;
            changeLabel.setText("M" + String.format("%.2f", change));
        } catch (NumberFormatException e) {
            changeLabel.setText("Invalid amount");
        }
    }

    private void handleCompleteOrder() {
        System.out.println("Order completed!");
    }

    private void handleClearOrder() {
        if (orderListView != null) {
            orderListView.getItems().clear();
        }
        if (totalLabel != null) {
            totalLabel.setText("M0.00");
        }
        if (cashField != null) {
            cashField.clear();
        }
        if (changeLabel != null) {
            changeLabel.setText("M0.00");
        }
    }
}