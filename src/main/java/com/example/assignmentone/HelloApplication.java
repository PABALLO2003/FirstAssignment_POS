package com.example.assignmentone;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        showWelcomeScreen(primaryStage);
    }

    private void showWelcomeScreen(Stage stage) {
        BorderPane root = new BorderPane();

        try {
            java.io.InputStream imageStream = getClass().getResourceAsStream("/MENU3.png");
            Image bgImage = null;
            if (imageStream != null) {
                bgImage = new Image(imageStream);
            }

            if (bgImage != null) {
                BackgroundImage background = new BackgroundImage(bgImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, true));
                root.setBackground(new Background(background));
            } else {
                root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
            }
        } catch (Exception ex) {
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        }

        Label logoLabel = new Label("🍽️");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 90));
        logoLabel.setTextFill(Color.GOLD);
        logoLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 20, 0.5, 0, 0);");

        Circle circle = new Circle(70, Color.rgb(255, 215, 0, 0.4));
        StackPane logoPane = new StackPane(circle, logoLabel);
        logoPane.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.7)));

        VBox topBox = new VBox(logoPane);
        topBox.setAlignment(Pos.TOP_CENTER);
        topBox.setPadding(new Insets(50, 0, 0, 0));
        root.setTop(topBox);

        VBox bottomBox = new VBox(20);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.setPadding(new Insets(0, 0, 80, 0));

        Label tagline = new Label("Serving the tastiest meals with love ❤️");
        tagline.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        tagline.setTextFill(Color.WHITE);
        tagline.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.7, 2, 2);");

        Button enterBtn = createEnterButton(stage);

        bottomBox.getChildren().addAll(tagline, enterBtn);

        bottomBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); -fx-background-radius: 30;");
        bottomBox.setMaxWidth(500);
        bottomBox.setPadding(new Insets(20, 30, 30, 30));

        HBox bottomContainer = new HBox(bottomBox);
        bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
        bottomContainer.setPadding(new Insets(0, 0, 50, 0));
        root.setBottom(bottomContainer);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        Scene scene = new Scene(root, 1300, 850);
        stage.setTitle("Welcome To Pablw's Yummy Delicacy POS");
        stage.setScene(scene);
        stage.show();
    }

    private Button createEnterButton(Stage stage) {
        Button enterBtn = new Button("ENTER RESTAURANT 🍴🍽️");
        enterBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 30; -fx-font-size: 18px; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 25;");
        enterBtn.setEffect(new DropShadow(12, Color.BLACK));
        enterBtn.setMaxWidth(300);

        enterBtn.setOnMouseEntered(_ -> {
            enterBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 30; -fx-font-size: 18px; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 25;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), enterBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        enterBtn.setOnMouseExited(_ -> {
            enterBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 30; -fx-font-size: 18px; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 25;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), enterBtn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        enterBtn.setOnAction(_ -> showMainApp(stage));

        return enterBtn;
    }

    private void showMainApp(Stage stage) {
        FoodRestaurantApp restaurantApp = new FoodRestaurantApp();
        try {
            restaurantApp.start(stage);
        } catch (Exception ex) {
            System.err.println("Error starting main application: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch();
    }
}