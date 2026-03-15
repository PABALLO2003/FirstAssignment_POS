package com.example.assignmentone;

import javafx.scene.effect.DropShadow;
import javafx.animation.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.sql.*;

public class FoodRestaurantApp {
    private final String[][] dinnerItems = {
            {"Rice+Beef", "45.00", "rice and beef1.jpg,rice and beef2.jpg,ricebeef3.jpg,beefrice1.jpg,beefrice2.jpg"},
            {"Rice+Chicken", "40.00", "HoneyChickenRice.jpg,chickenrice2.jpg,chickenrice3.jpg,chickenrice4.jpg,chickenrice5.jpg,chicken.jpg,ricechicken1.jpg,ChickenRice1.jpg"},
            {"Marcan+Beef", "55.00", "marcan1.jpeg,marcan2.jpg,marcan3.jpg,marcan.jpeg,beefmorcon1.jpg,beefmorcon2.jpg,morconbeef.jpg,beefmorcon.jpg"}
    };

    private final String[][] dessertItems = {
            {"Custard and Jelly", "25.00", "custardjelly.jpeg,custardjelly1.jpg,custardjelly2.jpg,custard1.jpg,custard2.jpg,jelly1.jpg"},
            {"Chocolate cake slice", "30.00", "chocolatecake.jpg,chocolatecake1.jpg,chocolatecake2.jpeg,chocolatecake3.jpg,cakeslice1.jpg,cakeslice2.jpg"},
            {"Cupcakes", "20.00", "cupcake.jpg,cupcake1.jpg,cupcake2.jpg,cupcake3.jpg,cupcake4.jpg,cupcake5.jpg,cupcake6.jpg"}
    };

    private final String[][] drinksItems = {
            {"Wine", "60.00", "wine.jpeg,wine1.jpeg,wine2.jpg,wine3.jpg,wine4.jpg,redwine.jpg"},
            {"Coca Cola", "18.00", "coke.jpeg,coke1.jpeg,cola2.jpg,cola3.jpeg,cola4.jpg,cocacola1.jpg"},
            {"Water", "15.00", "water.jpg,water1.jpg,water2.jpeg,water3.jpeg,waterbottle.jpg,mineralwater.jpg"},
            {"Juice", "35.00", "drinks.jpg,drinks1.jpg,drinks2.jpg,drinks3.jpg,drinks4.jpeg,drinks5.jpg"}
    };

    private final ObservableList<OrderItemDisplay> orderItems = FXCollections.observableArrayList();
    private final ObservableList<ImageView> orderImages = FXCollections.observableArrayList();
    private double totalAmount = 0.0;
    private double cashTendered = 0.0;
    private double changeGiven = 0.0;
    private Label totalLabel;
    private Label timeLabel;
    private Label changeLabel;
    private Label userLabel;
    private Label clientLabel;
    private TextField cashField;
    private TextField clientField;
    private ListView<OrderItemDisplay> orderListView;
    private Stage mainStage;
    private String currentUser = "";
    private String currentBackgroundColor = "linear-gradient(to bottom, #ff69b4, #ff1493)";
    private VBox loginBox;
    private MenuButton drinksMenu;
    private Label selectedDrinkLabel;
    private ImageView selectedDrinkImage;
    private final ToggleGroup dessertGroup = new ToggleGroup();
    private final Map<String, CheckBox> dinnerCheckBoxes = new HashMap<>();
    private final Map<String, RadioButton> dessertRadioButtons = new HashMap<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Pablws_Yummy_Delicacy";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Livingbymywords200359655410@";

    public void start(Stage stage) {
        showStaffLogin(stage);
    }

    private void showStaffLogin(Stage stage) {
        loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("-fx-background-color: " + currentBackgroundColor + "; -fx-padding: 40;");

        Button backBtn = createBackButton(stage);
        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(0, 0, 20, 0));

        HBox emojiBox = createEmojiBox();
        HBox colorPickerBox = createColorPickerBox();

        Label logoIcon = new Label("🍽️");
        logoIcon.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        logoIcon.setTextFill(Color.GOLD);
        logoIcon.setEffect(new DropShadow(20, Color.BLACK));

        Label title = new Label("🔐 STAFF LOGIN");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.GOLD);
        title.setEffect(new DropShadow(5, Color.BLACK));

        Label userPromptLabel = new Label("USERNAME:");
        userPromptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        userPromptLabel.setTextFill(Color.WHITE);
        userPromptLabel.setEffect(new DropShadow(3, Color.BLACK));

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(320);
        usernameField.setPromptText("Enter username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 30; -fx-background-color: rgba(255,255,255,0.9);");

        Label passPromptLabel = new Label("PASSWORD:");
        passPromptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        passPromptLabel.setTextFill(Color.WHITE);
        passPromptLabel.setEffect(new DropShadow(3, Color.BLACK));

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(320);
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 12; -fx-background-radius: 30; -fx-background-color: rgba(255,255,255,0.9);");

        Button loginBtn = createLoginButton();
        Label message = new Label("");

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), loginBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        ScaleTransition scaleLogo = new ScaleTransition(Duration.seconds(0.7), logoIcon);
        scaleLogo.setFromX(0.5);
        scaleLogo.setFromY(0.5);
        scaleLogo.setToX(1);
        scaleLogo.setToY(1);
        scaleLogo.play();

        loginBtn.setOnAction(_ -> handleLogin(stage, usernameField, passwordField, message));

        loginBox.getChildren().addAll(
                backBox,
                emojiBox,
                colorPickerBox,
                logoIcon,
                title,
                userPromptLabel,
                usernameField,
                passPromptLabel,
                passwordField,
                loginBtn,
                message
        );
        Scene scene = new Scene(loginBox, 1200, 900);
        stage.setScene(scene);
        stage.setTitle("Pablw's Yummy Delicacy POS - Staff Login");
        stage.show();
    }

    private Button createBackButton(Stage stage) {
        Button backBtn = new Button("← BACK TO HOME");
        backBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 20; -fx-cursor: hand;");
        backBtn.setAlignment(Pos.TOP_LEFT);
        backBtn.setOnAction(_ -> {
            HelloApplication helloApp = new HelloApplication();
            try {
                helloApp.start(stage);
            } catch (Exception e) {
                System.err.println("Error returning to home: " + e.getMessage());
            }
        });
        return backBtn;
    }

    private HBox createEmojiBox() {
        HBox emojiBox = new HBox(15);
        emojiBox.setAlignment(Pos.CENTER);
        emojiBox.setPadding(new Insets(20, 0, 20, 0));
        emojiBox.setStyle("-fx-background-color: transparent;");
        String[] foodEmojis = {
                "🍔", "🍕", "🍟", "🌮", "🥪",
                "🥗", "🍰", "🥤", "🍸", "🍹",
                "🍣", "🍜", "🍪", "🍩", "☕",
                "🥩", "🍗", "🥘", "🍝", "🥙",
                "🥟", "🥠", "🥡", "🍦", "🍧"
        };
        for (int i = 0; i < foodEmojis.length; i++) {
            Text emoji = new Text(foodEmojis[i]);
            emoji.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 48));
            emoji.setFill(Color.WHITE);
            emoji.setStyle("-fx-font-smoothing-type: lcd;");
            DropShadow ds = new DropShadow();
            ds.setRadius(5.0);
            ds.setOffsetX(2.0);
            ds.setOffsetY(2.0);
            ds.setColor(Color.BLACK);
            emoji.setEffect(ds);

            Animation animation = switch (i % 4) {
                case 0 -> {
                    TranslateTransition bounce = new TranslateTransition(Duration.seconds(1.2), emoji);
                    bounce.setFromY(0);
                    bounce.setToY(-20);
                    bounce.setCycleCount(Animation.INDEFINITE);
                    bounce.setAutoReverse(true);
                    bounce.setDelay(Duration.millis(i * 80));
                    yield bounce;
                }
                case 1 -> {
                    RotateTransition rotate = new RotateTransition(Duration.seconds(1.8), emoji);
                    rotate.setByAngle(15);
                    rotate.setCycleCount(Animation.INDEFINITE);
                    rotate.setAutoReverse(true);
                    rotate.setDelay(Duration.millis(i * 80));
                    yield rotate;
                }
                case 2 -> {
                    ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), emoji);
                    scale.setFromX(1.0);
                    scale.setFromY(1.0);
                    scale.setToX(1.3);
                    scale.setToY(1.3);
                    scale.setCycleCount(Animation.INDEFINITE);
                    scale.setAutoReverse(true);
                    scale.setDelay(Duration.millis(i * 80));
                    yield scale;
                }
                default -> {
                    TranslateTransition side = new TranslateTransition(Duration.seconds(2.0), emoji);
                    side.setFromX(0);
                    side.setToX(15);
                    side.setCycleCount(Animation.INDEFINITE);
                    side.setAutoReverse(true);
                    side.setDelay(Duration.millis(i * 80));
                    yield side;
                }
            };
            animation.play();
            emojiBox.getChildren().add(emoji);
        }
        return emojiBox;
    }

    private HBox createColorPickerBox() {
        HBox colorPickerBox = new HBox(10);
        colorPickerBox.setAlignment(Pos.CENTER);
        colorPickerBox.setPadding(new Insets(10, 0, 20, 0));
        colorPickerBox.setStyle("-fx-background-color: transparent;");

        Label colorLabel = new Label("Background:");
        colorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        colorLabel.setTextFill(Color.WHITE);
        colorLabel.setEffect(new DropShadow(3, Color.BLACK));

        Button color1Btn = createColorButton("Pink", "linear-gradient(to bottom, #ff69b4, #ff1493)");
        Button color2Btn = createColorButton("Blue", "linear-gradient(to bottom, #2c3e50, #3498db)");
        Button color3Btn = createColorButton("Green", "linear-gradient(to bottom, #1e3c2c, #2ecc71)");
        Button color4Btn = createColorButton("Purple", "linear-gradient(to bottom, #4a235a, #9b59b6)");
        Button color5Btn = createColorButton("Orange", "linear-gradient(to bottom, #d35400, #f39c12)");
        Button color6Btn = createColorButton("Red", "linear-gradient(to bottom, #922b21, #e74c3c)");

        colorPickerBox.getChildren().addAll(colorLabel, color1Btn, color2Btn, color3Btn, color4Btn, color5Btn, color6Btn);
        return colorPickerBox;
    }

    private Button createColorButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 20;");
        btn.setOnAction(_ -> changeBackgroundColor(color));
        return btn;
    }

    private Button createLoginButton() {
        Button loginBtn = new Button("LOGIN & CONTINUE");
        loginBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 15 40; -fx-background-radius: 35; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 35;");
        loginBtn.setEffect(new DropShadow(15, Color.GREEN));

        loginBtn.setOnMouseEntered(_ -> {
            loginBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 15 40; -fx-background-radius: 35; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 35;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), loginBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        loginBtn.setOnMouseExited(_ -> {
            loginBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 15 40; -fx-background-radius: 35; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 35;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), loginBtn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return loginBtn;
    }

    private void handleLogin(Stage stage, TextField usernameField, PasswordField passwordField, Label message) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (authenticateUser(username, password)) {
            currentUser = username;
            startMainApp(stage);
        } else {
            message.setText("❌ Invalid username or password!");
            message.setTextFill(Color.RED);
            message.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            Timeline shake = new Timeline(
                    new KeyFrame(Duration.millis(50), _ -> loginBox.setTranslateX(8)),
                    new KeyFrame(Duration.millis(100), _ -> loginBox.setTranslateX(-8)),
                    new KeyFrame(Duration.millis(150), _ -> loginBox.setTranslateX(8)),
                    new KeyFrame(Duration.millis(200), _ -> loginBox.setTranslateX(-8)),
                    new KeyFrame(Duration.millis(250), _ -> loginBox.setTranslateX(4)),
                    new KeyFrame(Duration.millis(300), _ -> loginBox.setTranslateX(-4)),
                    new KeyFrame(Duration.millis(350), _ -> loginBox.setTranslateX(0))
            );
            shake.play();
        }
    }

    private void changeBackgroundColor(String color) {
        currentBackgroundColor = color;
        loginBox.setStyle("-fx-background-color: " + color + "; -fx-padding: 40;");
    }

    private boolean authenticateUser(String username, String password) {
        return (username.equals("Paballo") && password.equals("pablw2026")) ||
                (username.equals("Ntsoaki") && password.equals("ntsoaki2026")) ||
                (username.equals("Maitu") && password.equals("maitu2026"));
    }

    private void startMainApp(Stage stage) {
        mainStage = stage;
        BorderPane root = new BorderPane();

        setBackgroundImage(root);

        HBox header = createHeader();
        root.setTop(header);

        TabPane menuTabs = new TabPane();
        menuTabs.setStyle("-fx-background-color: rgba(236, 240, 241, 0.8); -fx-background-radius: 10;");

        Tab dinnerTab = createDinnerTab();
        Tab dessertTab = createDessertTab();
        Tab drinksTab = createDrinksTab();
        menuTabs.getTabs().addAll(dinnerTab, dessertTab, drinksTab);

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.getChildren().addAll(menuTabs, createOrderSection());
        root.setCenter(centerContent);
        root.setRight(createPaymentSection());

        startTimeUpdate();

        Scene scene = new Scene(root, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Pablw's Yummy Delicacy - Logged in as: " + currentUser);
        stage.show();
    }

    private void setBackgroundImage(BorderPane root) {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/food1.jpg");
            if (imageStream != null) {
                Image bgImage = new Image(imageStream);
                BackgroundImage background = new BackgroundImage(bgImage,
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, true));
                root.setBackground(new Background(background));
            } else {
                root.setStyle("-fx-background-color: #f0f0f0;");
            }
        } catch (Exception ex) {
            root.setStyle("-fx-background-color: #f0f0f0;");
        }
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: rgba(44, 62, 80, 0.7); -fx-background-radius: 0 0 10 10;");

        Label logoText = new Label("🍔 Pablw's Yummy Delicacy 🍕");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        logoText.setTextFill(Color.GOLD);
        logoText.setEffect(new DropShadow(5, Color.BLACK));

        userLabel = new Label("👤 SERVER: " + currentUser);
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userLabel.setTextFill(Color.CYAN);
        userLabel.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 5 10; -fx-background-radius: 15;");

        timeLabel = new Label();
        timeLabel.setFont(Font.font("Arial", 16));
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setEffect(new DropShadow(3, Color.BLACK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(logoText, userLabel, spacer, timeLabel);
        return header;
    }

    private Tab createDinnerTab() {
        Tab tab = new Tab("🍽️ DINNER");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        Label instructionLabel = new Label("Click on any picture OR use checkboxes to select dinner items");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        instructionLabel.setTextFill(Color.DARKBLUE);
        instructionLabel.setPadding(new Insets(10));
        instructionLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 5;");
        mainContainer.getChildren().add(instructionLabel);

        GridPane grid = createDinnerGrid();
        mainContainer.getChildren().add(grid);
        scrollPane.setContent(mainContainer);
        tab.setContent(scrollPane);
        return tab;
    }

    private GridPane createDinnerGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));

        String[] dinnerColors = {"#FF6B6B", "#4ECDC4", "#45B7D1"};
        int row = 0, col = 0;
        int colorIndex = 0;

        for (String[] item : dinnerItems) {
            String itemName = item[0];
            double itemPrice = Double.parseDouble(item[1]);
            String itemImages = item[2];

            VBox box = createClickableMenuItemWithPictures(
                    itemName, itemPrice, "Dinner", itemImages,
                    dinnerColors[colorIndex % dinnerColors.length], true
            );

            CheckBox selectBox = new CheckBox("Select");
            selectBox.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            dinnerCheckBoxes.put(itemName, selectBox);

            selectBox.setOnAction(_ -> handleDinnerCheckbox(itemName, itemPrice, itemImages, selectBox));

            box.getChildren().add(selectBox);
            colorIndex++;
            grid.add(box, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    private void handleDinnerCheckbox(String itemName, double itemPrice, String itemImages, CheckBox selectBox) {
        if (selectBox.isSelected()) {
            String[] images = itemImages.split(",");
            String firstImage = images.length > 0 ? images[0].trim() : "";
            String itemNameWithImage = itemName + " (" + firstImage + ")";
            addToOrderWithImage(itemNameWithImage, itemPrice, "Dinner", firstImage, itemImages);
        } else {
            removeSpecificItem("Dinner", itemName, itemPrice);
        }
    }

    private Tab createDessertTab() {
        Tab tab = new Tab("🍰 DESSERT");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        Label instructionLabel = new Label("Click on any picture OR use radio buttons to select dessert (only one allowed)");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        instructionLabel.setTextFill(Color.DARKBLUE);
        instructionLabel.setPadding(new Insets(10));
        instructionLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 5;");
        mainContainer.getChildren().add(instructionLabel);

        GridPane grid = createDessertGrid();
        mainContainer.getChildren().add(grid);
        scrollPane.setContent(mainContainer);
        tab.setContent(scrollPane);
        return tab;
    }

    private GridPane createDessertGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));

        String[] dessertColors = {"#FFB347", "#9B59B6", "#FF8C94"};
        int row = 0, col = 0;
        int colorIndex = 0;

        for (String[] item : dessertItems) {
            String itemName = item[0];
            double itemPrice = Double.parseDouble(item[1]);
            String itemImages = item[2];

            VBox box = createClickableMenuItemWithPictures(
                    itemName, itemPrice, "Dessert", itemImages,
                    dessertColors[colorIndex % dessertColors.length], false
            );

            RadioButton selectRadio = new RadioButton("Select");
            selectRadio.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            selectRadio.setToggleGroup(dessertGroup);
            dessertRadioButtons.put(itemName, selectRadio);

            selectRadio.setOnAction(_ -> handleDessertRadio(itemName, itemPrice, itemImages));

            box.getChildren().add(selectRadio);
            colorIndex++;
            grid.add(box, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    private void handleDessertRadio(String itemName, double itemPrice, String itemImages) {
        ObservableList<OrderItemDisplay> toRemove = FXCollections.observableArrayList();
        ObservableList<ImageView> imgToRemove = FXCollections.observableArrayList();
        for (OrderItemDisplay s : orderItems) {
            if (s.category().startsWith("Dessert")) toRemove.add(s);
        }
        for (ImageView img : orderImages) {
            if (img.getUserData() != null && img.getUserData().toString().contains("Dessert")) {
                imgToRemove.add(img);
            }
        }
        orderItems.removeAll(toRemove);
        orderImages.removeAll(imgToRemove);
        recalculateTotal();

        String[] images = itemImages.split(",");
        String firstImage = images.length > 0 ? images[0].trim() : "";
        String itemNameWithImage = itemName + " (" + firstImage + ")";
        addToOrderWithImage(itemNameWithImage, itemPrice, "Dessert", firstImage, itemImages);
    }

    private Tab createDrinksTab() {
        Tab tab = new Tab("🥤 DRINKS");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        Label instructionLabel = new Label("Click on any picture OR use the dropdown menu to select drink (only one allowed)");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        instructionLabel.setTextFill(Color.DARKBLUE);
        instructionLabel.setPadding(new Insets(10));
        instructionLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 5;");
        mainContainer.getChildren().add(instructionLabel);

        drinksMenu = new MenuButton("🥤 SELECT DRINK");
        drinksMenu.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 25; -fx-background-radius: 30; -fx-cursor: hand;");
        drinksMenu.setMaxWidth(300);

        HBox menuBox = new HBox(drinksMenu);
        menuBox.setAlignment(Pos.CENTER);
        mainContainer.getChildren().add(menuBox);

        VBox previewBox = createDrinkPreviewBox();
        mainContainer.getChildren().add(previewBox);

        populateDrinksMenu();

        Label picLabel = new Label("Or click on any drink picture:");
        picLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        picLabel.setStyle("-fx-text-fill: black;");
        mainContainer.getChildren().add(picLabel);

        GridPane grid = createDrinksGrid();
        mainContainer.getChildren().add(grid);

        scrollPane.setContent(mainContainer);
        tab.setContent(scrollPane);
        return tab;
    }

    private VBox createDrinkPreviewBox() {
        VBox previewBox = new VBox(15);
        previewBox.setAlignment(Pos.CENTER);
        previewBox.setPadding(new Insets(20));
        previewBox.setStyle("-fx-background-color: rgba(52, 152, 219, 0.1); -fx-background-radius: 10;");

        selectedDrinkLabel = new Label("No drink selected");
        selectedDrinkLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        selectedDrinkLabel.setStyle("-fx-text-fill: black;");

        selectedDrinkImage = new ImageView();
        selectedDrinkImage.setFitHeight(150);
        selectedDrinkImage.setFitWidth(150);
        selectedDrinkImage.setPreserveRatio(true);
        selectedDrinkImage.setVisible(false);

        previewBox.getChildren().addAll(selectedDrinkLabel, selectedDrinkImage);
        return previewBox;
    }

    private void populateDrinksMenu() {
        for (String[] item : drinksItems) {
            String itemName = item[0];
            double itemPrice = Double.parseDouble(item[1]);
            String itemImages = item[2];

            MenuItem menuItem = new MenuItem(itemName + " - M" + String.format("%.2f", itemPrice));

            String[] images = itemImages.split(",");
            if (images.length > 0) {
                String firstImage = images[0].trim();
                InputStream imageStream = getClass().getResourceAsStream("/" + firstImage);
                if (imageStream != null) {
                    Image img = new Image(imageStream);
                    ImageView iconView = new ImageView(img);
                    iconView.setFitHeight(20);
                    iconView.setFitWidth(20);
                    iconView.setPreserveRatio(true);
                    menuItem.setGraphic(iconView);
                }
            }

            menuItem.setOnAction(_ -> selectDrink(itemName, itemPrice, itemImages));
            drinksMenu.getItems().add(menuItem);
        }

        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem clearItem = new MenuItem("Clear Selection");
        clearItem.setStyle("-fx-text-fill: #e74c3c;");
        clearItem.setOnAction(_ -> clearDrinkSelection());
        drinksMenu.getItems().addAll(separator, clearItem);
    }

    private GridPane createDrinksGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        String[] drinkColors = {"#3498DB", "#1ABC9C", "#F1C40F", "#E67E22"};
        int row = 0, col = 0;
        int colorIndex = 0;

        for (String[] item : drinksItems) {
            VBox box = createClickableMenuItemWithPictures(
                    item[0], Double.parseDouble(item[1]), "Drink", item[2],
                    drinkColors[colorIndex % drinkColors.length], false
            );
            colorIndex++;
            grid.add(box, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    private VBox createClickableMenuItemWithPictures(String name, double price, String category, String imageList, String bgColor, boolean multipleAllowed) {
        VBox mainBox = new VBox(10);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(15));
        mainBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);

        Label priceLabel = new Label("M" + String.format("%.2f", price));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.GOLD);

        Label catLabel = new Label(category);
        catLabel.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 5; -fx-padding: 2 8;");
        catLabel.setTextFill(Color.WHITE);

        FlowPane pictureFlow = createPictureFlow(name, price, category, imageList, multipleAllowed);

        mainBox.getChildren().addAll(nameLabel, priceLabel, catLabel, pictureFlow);
        return mainBox;
    }

    private FlowPane createPictureFlow(String name, double price, String category, String imageList, boolean multipleAllowed) {
        FlowPane pictureFlow = new FlowPane();
        pictureFlow.setHgap(10);
        pictureFlow.setVgap(10);
        pictureFlow.setAlignment(Pos.CENTER);
        pictureFlow.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 5; -fx-padding: 5;");

        if (!imageList.isEmpty()) {
            String[] images = imageList.split(",");
            for (String imgFile : images) {
                String trimmedFile = imgFile.trim();
                InputStream imageStream = getClass().getResourceAsStream("/" + trimmedFile);
                if (imageStream != null) {
                    Image img = new Image(imageStream);
                    ImageView imgView = createImageView(img, name, price, category, trimmedFile, imageList, multipleAllowed);
                    pictureFlow.getChildren().add(imgView);
                }
            }
        }
        return pictureFlow;
    }

    private ImageView createImageView(Image img, String name, double price, String category, String trimmedFile, String imageList, boolean multipleAllowed) {
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(70);
        imgView.setFitWidth(70);
        imgView.setPreserveRatio(true);
        imgView.setStyle("-fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");

        ImageItemData itemData = new ImageItemData(name, price, category, trimmedFile, imageList);
        imgView.setUserData(itemData);

        imgView.setOnMouseClicked(_ -> handleImageClick(imgView, img, category, multipleAllowed));

        return imgView;
    }

    private void handleImageClick(ImageView imgView, Image img, String category, boolean multipleAllowed) {
        Pane pictureFlow = (Pane) imgView.getParent();
        if (!multipleAllowed && (category.equals("Dessert") || category.equals("Drink"))) {
            resetBordersInContainer(pictureFlow);
        }

        imgView.setStyle("-fx-cursor: hand; -fx-border-color: gold; -fx-border-width: 3; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, gold, 10, 0, 0, 0);");

        ImageItemData data = (ImageItemData) imgView.getUserData();

        switch (category) {
            case "Dinner" -> handleDinnerImageClick(data);
            case "Dessert" -> handleDessertImageClick(data);
            case "Drink" -> handleDrinkImageClick(data, img);
        }

        showImagePreview(data.imageFile, data.name + " selected!");
    }

    private void handleDinnerImageClick(ImageItemData data) {
        String itemNameWithImage = data.name + " (" + data.imageFile + ")";
        addToOrderWithImage(itemNameWithImage, data.price, data.category, data.imageFile, data.imageList);
        if (dinnerCheckBoxes.containsKey(data.name)) {
            dinnerCheckBoxes.get(data.name).setSelected(true);
        }
    }

    private void handleDessertImageClick(ImageItemData data) {
        removeCategoryItems("Dessert");
        String itemNameWithImage = data.name + " (" + data.imageFile + ")";
        addToOrderWithImage(itemNameWithImage, data.price, data.category, data.imageFile, data.imageList);
        if (dessertRadioButtons.containsKey(data.name)) {
            dessertRadioButtons.get(data.name).setSelected(true);
        }
    }

    private void handleDrinkImageClick(ImageItemData data, Image img) {
        removeCategoryItems("Drink");
        String itemNameWithImage = data.name + " (" + data.imageFile + ")";
        addToOrderWithImage(itemNameWithImage, data.price, data.category, data.imageFile, data.imageList);
        if (drinksMenu != null) {
            drinksMenu.setText("✓ " + data.name);
        }
        if (selectedDrinkLabel != null) {
            selectedDrinkLabel.setText(data.name + " - M" + String.format("%.2f", data.price));
        }
        if (selectedDrinkImage != null) {
            selectedDrinkImage.setImage(img);
            selectedDrinkImage.setVisible(true);
        }
    }

    private void resetBordersInContainer(Pane container) {
        for (Node node : container.getChildren()) {
            if (node instanceof ImageView) {
                node.setStyle("-fx-cursor: hand; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");
            }
        }
    }

    private record ImageItemData(String name, double price, String category, String imageFile, String imageList) {}

    private void removeCategoryItems(String category) {
        ObservableList<OrderItemDisplay> toRemove = FXCollections.observableArrayList();
        ObservableList<ImageView> imgToRemove = FXCollections.observableArrayList();

        for (OrderItemDisplay item : orderItems) {
            if (item.category().startsWith(category)) {
                toRemove.add(item);
            }
        }
        for (ImageView img : orderImages) {
            if (img.getUserData() != null && img.getUserData().toString().contains(category)) {
                imgToRemove.add(img);
            }
        }
        orderItems.removeAll(toRemove);
        orderImages.removeAll(imgToRemove);
        recalculateTotal();
    }

    private void selectDrink(String name, double price, String imageList) {
        removeCategoryItems("Drink");
        String[] images = imageList.split(",");
        String firstImage = images.length > 0 ? images[0].trim() : "";
        String itemNameWithImage = name + " (" + firstImage + ")";
        addToOrderWithImage(itemNameWithImage, price, "Drink", firstImage, imageList);

        if (drinksMenu != null) {
            drinksMenu.setText("✓ " + name);
        }
        if (selectedDrinkLabel != null) {
            selectedDrinkLabel.setText(name + " - M" + String.format("%.2f", price));
        }
        if (selectedDrinkImage != null && !firstImage.isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream("/" + firstImage);
            if (imageStream != null) {
                Image img = new Image(imageStream);
                selectedDrinkImage.setImage(img);
                selectedDrinkImage.setVisible(true);
            }
        }
    }

    private void clearDrinkSelection() {
        removeCategoryItems("Drink");
        if (drinksMenu != null) {
            drinksMenu.setText("🥤 SELECT DRINK");
        }
        if (selectedDrinkLabel != null) {
            selectedDrinkLabel.setText("No drink selected");
        }
        if (selectedDrinkImage != null) {
            selectedDrinkImage.setVisible(false);
        }
    }

    private void showImagePreview(String imageFile, String message) {
        Stage previewStage = new Stage();
        previewStage.setTitle("Food Preview");

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");

        InputStream imageStream = getClass().getResourceAsStream("/" + imageFile);
        if (imageStream != null) {
            Image img = new Image(imageStream);
            ImageView bigView = new ImageView(img);
            bigView.setFitHeight(250);
            bigView.setFitWidth(250);
            bigView.setPreserveRatio(true);
            bigView.setStyle("-fx-border-color: gold; -fx-border-width: 3; -fx-border-radius: 10;");

            Label msgLabel = new Label(message);
            msgLabel.setTextFill(Color.GOLD);
            msgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Label fileName = new Label(imageFile);
            fileName.setTextFill(Color.WHITE);
            fileName.setFont(Font.font("Arial", 12));

            Button closeBtn = new Button("CLOSE");
            closeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5;");
            closeBtn.setOnAction(_ -> previewStage.close());

            vbox.getChildren().addAll(bigView, msgLabel, fileName, closeBtn);

            Scene scene = new Scene(vbox, 400, 450);
            previewStage.setScene(scene);
            previewStage.show();
        }
    }

    private VBox createOrderSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 10; -fx-border-color: #bdc3c7; -fx-border-radius: 10;");

        Label title = new Label("CURRENT ORDER");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.DARKBLUE);

        HBox clientBox = createClientBox();

        Label imageTitle = new Label("Selected Items:");
        imageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        imageTitle.setTextFill(Color.DARKBLUE);

        HBox imageContainer = createImageContainer();

        orderListView = new ListView<>(orderItems);
        orderListView.setPrefHeight(200);
        orderListView.setCellFactory(_ -> createOrderCell());

        HBox totalBox = createTotalBox();

        box.getChildren().addAll(title, clientBox, imageTitle, imageContainer, orderListView, totalBox);
        return box;
    }

    private HBox createClientBox() {
        HBox clientBox = new HBox(10);
        clientBox.setAlignment(Pos.CENTER_LEFT);
        clientBox.setPadding(new Insets(5, 0, 5, 0));

        Label clientNameLabel = new Label("CLIENT NAME:");
        clientNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clientNameLabel.setTextFill(Color.DARKBLUE);

        clientField = new TextField();
        clientField.setPromptText("Enter client name");
        clientField.setPrefWidth(200);
        clientField.setStyle("-fx-background-radius: 5; -fx-padding: 5;");

        clientLabel = new Label("");
        clientLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clientLabel.setTextFill(Color.GREEN);

        clientBox.getChildren().addAll(clientNameLabel, clientField, clientLabel);
        return clientBox;
    }

    private HBox createImageContainer() {
        HBox imageContainer = new HBox(10);
        imageContainer.setAlignment(Pos.CENTER_LEFT);
        imageContainer.setPadding(new Insets(5));
        imageContainer.setStyle("-fx-background-color: rgba(236, 240, 241, 0.9); -fx-background-radius: 5;");
        imageContainer.setPrefHeight(80);

        orderImages.addListener((ListChangeListener<ImageView>) change -> {
            imageContainer.getChildren().clear();
            for (ImageView img : orderImages) {
                ImageView smallImg = new ImageView(img.getImage());
                smallImg.setFitHeight(50);
                smallImg.setFitWidth(50);
                smallImg.setPreserveRatio(true);
                smallImg.setStyle("-fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 5;");
                imageContainer.getChildren().add(smallImg);
            }
        });

        return imageContainer;
    }

    private ListCell<OrderItemDisplay> createOrderCell() {
        return new ListCell<>() {
            private final Button removeBtn = new Button("✖");
            private final HBox container = new HBox(10);
            private final Label itemLabel = new Label();

            {
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 30; -fx-min-height: 25; -fx-background-radius: 5; -fx-cursor: hand;");
                removeBtn.setTooltip(new Tooltip("Remove this item"));
                itemLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5 0 5 5; -fx-text-fill: black;");

                removeBtn.setOnMouseEntered(_ -> removeBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 30; -fx-min-height: 25; -fx-background-radius: 5; -fx-cursor: hand;"));
                removeBtn.setOnMouseExited(_ -> removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 30; -fx-min-height: 25; -fx-background-radius: 5; -fx-cursor: hand;"));

                container.setAlignment(Pos.CENTER_LEFT);
                container.setPadding(new Insets(5));
                container.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                container.getChildren().addAll(itemLabel, spacer, removeBtn);
            }

            @Override
            protected void updateItem(OrderItemDisplay item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    itemLabel.setText(item.toString());
                    removeBtn.setOnAction(_ -> removeSelectedItem(item));
                    setGraphic(container);
                }
            }
        };
    }

    private HBox createTotalBox() {
        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10, 0, 0, 0));

        Label totalText = new Label("TOTAL AMOUNT:");
        totalText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        totalLabel = new Label("M0.00");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalLabel.setTextFill(Color.GREEN);
        totalLabel.setStyle("-fx-background-color: white; -fx-padding: 5 10; -fx-background-radius: 5; -fx-border-color: #27ae60;");

        totalBox.getChildren().addAll(totalText, totalLabel);
        return totalBox;
    }

    private VBox createPaymentSection() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(25));
        box.setPrefWidth(350);
        box.setStyle("-fx-background-color: rgba(52, 73, 94, 0.9); -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, black, 10, 0, 0, 5);");

        Label title = new Label("💳 PAYMENT");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.GOLD);

        HBox totalDisplayBox = createTotalDisplayBox();

        Label cashLabel = new Label("AMOUNT TENDERED (M):");
        cashLabel.setTextFill(Color.WHITE);
        cashLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        cashField = new TextField();
        cashField.setPromptText("Enter amount");
        cashField.setStyle("-fx-background-radius: 5; -fx-font-size: 14px; -fx-padding: 10;");

        Button calcBtn = new Button("CALCULATE CHANGE");
        calcBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 12; -fx-cursor: hand;");
        calcBtn.setMaxWidth(Double.MAX_VALUE);
        calcBtn.setOnAction(_ -> calculateChange());

        Label changeText = new Label("CHANGE:");
        changeText.setTextFill(Color.WHITE);
        changeText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        changeLabel = new Label("M0.00");
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        changeLabel.setTextFill(Color.web("#FF6666"));
        changeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 10 20; -fx-background-radius: 10;");

        HBox actionButtons = createActionButtons();

        Button completeBtn = new Button("COMPLETE ORDER");
        completeBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 12; -fx-cursor: hand;");
        completeBtn.setMaxWidth(Double.MAX_VALUE);
        completeBtn.setOnAction(_ -> completeOrder());

        box.getChildren().addAll(title, totalDisplayBox, cashLabel, cashField, calcBtn, changeText, changeLabel, actionButtons, completeBtn);
        return box;
    }

    private HBox createTotalDisplayBox() {
        HBox totalDisplayBox = new HBox(10);
        totalDisplayBox.setAlignment(Pos.CENTER_LEFT);
        totalDisplayBox.setPadding(new Insets(10));
        totalDisplayBox.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5;");

        Label totalDisplayText = new Label("TOTAL:");
        totalDisplayText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        totalDisplayText.setTextFill(Color.WHITE);

        Label totalDisplayValue = new Label();
        totalDisplayValue.textProperty().bind(totalLabel.textProperty());
        totalDisplayValue.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        totalDisplayValue.setTextFill(Color.GOLD);

        totalDisplayBox.getChildren().addAll(totalDisplayText, totalDisplayValue);
        return totalDisplayBox;
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(15, 0, 0, 0));

        Button resetBtn = new Button("RESET");
        resetBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 90; -fx-padding: 12; -fx-background-radius: 5; -fx-cursor: hand;");
        resetBtn.setTooltip(new Tooltip("Reset all items"));
        resetBtn.setOnAction(_ -> resetSystem());

        Button exitBtn = new Button("EXIT");
        exitBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80; -fx-padding: 12; -fx-background-radius: 5; -fx-cursor: hand;");
        exitBtn.setTooltip(new Tooltip("Close application"));
        exitBtn.setOnAction(_ -> exitApplication());

        actionButtons.getChildren().addAll(resetBtn, exitBtn);
        return actionButtons;
    }

    private void addToOrderWithImage(String name, double price, String category, String selectedImageFile, String imageList) {
        OrderItemDisplay displayItem = new OrderItemDisplay(category, name, price);
        orderItems.add(displayItem);
        totalAmount += price;
        totalLabel.setText("M" + String.format("%.2f", totalAmount));

        if (selectedImageFile != null && !selectedImageFile.isEmpty()) {
            InputStream imageStream = getClass().getResourceAsStream("/" + selectedImageFile);
            if (imageStream != null) {
                Image img = new Image(imageStream);
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(50);
                imgView.setFitWidth(50);
                imgView.setPreserveRatio(true);
                imgView.setUserData(category + ":" + selectedImageFile);
                orderImages.add(imgView);
            }
        }
    }

    private void removeSelectedItem(OrderItemDisplay item) {
        orderItems.remove(item);
        totalAmount -= item.price();
        totalLabel.setText("M" + String.format("%.2f", totalAmount));

        for (int i = 0; i < orderImages.size(); i++) {
            ImageView img = orderImages.get(i);
            if (img.getUserData() != null && img.getUserData().toString().startsWith(item.category())) {
                orderImages.remove(i);
                break;
            }
        }

        String itemName = item.name();
        String category = item.category();
        String baseName = extractBaseName(itemName);

        if (category.startsWith("Dinner") && dinnerCheckBoxes.containsKey(baseName)) {
            dinnerCheckBoxes.get(baseName).setSelected(false);
        } else if (category.startsWith("Dessert") && dessertRadioButtons.containsKey(baseName)) {
            dessertRadioButtons.get(baseName).setSelected(false);
        } else if (category.startsWith("Drink")) {
            clearDrinkSelection();
        }
    }

    private String extractBaseName(String itemName) {
        return itemName.contains("(") ? itemName.substring(0, itemName.indexOf("(")).trim() : itemName;
    }

    private void removeSpecificItem(String category, String name, double price) {
        for (OrderItemDisplay item : orderItems) {
            if (item.category().startsWith(category) && item.name().contains(name) && item.price() == price) {
                orderItems.remove(item);
                for (int i = 0; i < orderImages.size(); i++) {
                    ImageView img = orderImages.get(i);
                    if (img.getUserData() != null && img.getUserData().toString().contains(category)) {
                        orderImages.remove(i);
                        break;
                    }
                }
                totalAmount -= price;
                totalLabel.setText("M" + String.format("%.2f", totalAmount));
                break;
            }
        }
    }

    private void recalculateTotal() {
        totalAmount = orderItems.stream().mapToDouble(OrderItemDisplay::price).sum();
        totalLabel.setText("M" + String.format("%.2f", totalAmount));
    }

    private void resetSystem() {
        orderItems.clear();
        orderImages.clear();
        totalAmount = 0.0;
        cashTendered = 0.0;
        changeGiven = 0.0;
        totalLabel.setText("M0.00");
        cashField.clear();
        clientField.clear();
        clientLabel.setText("");
        changeLabel.setText("M0.00");
        changeLabel.setTextFill(Color.web("#FF6666"));

        dinnerCheckBoxes.values().forEach(cb -> cb.setSelected(false));
        dessertRadioButtons.values().forEach(rb -> rb.setSelected(false));

        if (drinksMenu != null) {
            drinksMenu.setText("🥤 SELECT DRINK");
        }
        if (selectedDrinkLabel != null) {
            selectedDrinkLabel.setText("No drink selected");
        }
        if (selectedDrinkImage != null) {
            selectedDrinkImage.setVisible(false);
        }
    }

    private void exitApplication() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Exit");
        confirm.setContentText("Are you sure you want to exit?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainStage.close();
            }
        });
    }

    private void calculateChange() {
        try {
            cashTendered = Double.parseDouble(cashField.getText());
            changeGiven = cashTendered - totalAmount;
            changeLabel.setText("M" + String.format("%.2f", changeGiven));
            changeLabel.setTextFill(Color.web("#FF6666"));
        } catch (Exception ex) {
            changeLabel.setText("Invalid");
            changeLabel.setTextFill(Color.RED);
        }
    }

    private boolean saveOrderToDatabase(String clientName) {
        String orderSql = "INSERT INTO orders (server_name, client_name, order_date, total_amount, cash_tendered, change_given, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, category, item_name, price) VALUES (?, ?, ?, ?)";
        String logSql = "INSERT INTO transaction_logs (order_id, action, action_time, details) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            int orderId = insertOrder(conn, orderSql, clientName, timestamp);
            insertOrderItems(conn, itemSql, orderId);
            insertTransactionLog(conn, logSql, orderId, timestamp);

            conn.commit();
            return true;

        } catch (SQLException e) {
            showAlert("Database Error", "Error saving to database: " + e.getMessage());
            return false;
        }
    }

    private int insertOrder(Connection conn, String orderSql, String clientName, Timestamp timestamp) throws SQLException {
        try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
            orderStmt.setString(1, currentUser);
            orderStmt.setString(2, clientName);
            orderStmt.setTimestamp(3, timestamp);
            orderStmt.setDouble(4, totalAmount);
            orderStmt.setDouble(5, cashTendered);
            orderStmt.setDouble(6, changeGiven);
            orderStmt.setString(7, "Completed");

            orderStmt.executeUpdate();

            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
    }

    private void insertOrderItems(Connection conn, String itemSql, int orderId) throws SQLException {
        try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
            for (OrderItemDisplay item : orderItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setString(2, item.category());
                itemStmt.setString(3, item.name());
                itemStmt.setDouble(4, item.price());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();
        }
    }

    private void insertTransactionLog(Connection conn, String logSql, int orderId, Timestamp timestamp) throws SQLException {
        try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
            logStmt.setInt(1, orderId);
            logStmt.setString(2, "ORDER_COMPLETED");
            logStmt.setTimestamp(3, timestamp);

            String details = String.format("Total: M%.2f, Cash: M%.2f, Change: M%.2f, Items: %d",
                    totalAmount, cashTendered, changeGiven, orderItems.size());

            logStmt.setString(4, details);
            logStmt.executeUpdate();
        }
    }

    private void showReceipt(String clientName) {
        Stage receiptStage = new Stage();
        receiptStage.setTitle("Order Receipt");
        receiptStage.initModality(Modality.APPLICATION_MODAL);

        VBox receiptLayout = new VBox(10);
        receiptLayout.setPadding(new Insets(20));
        receiptLayout.setStyle("-fx-background-color: white; -fx-border-color: #2c3e50; -fx-border-width: 2; -fx-border-radius: 10;");
        receiptLayout.setAlignment(Pos.CENTER);
        receiptLayout.setPrefWidth(350);

        Label titleLabel = new Label("PABLW'S YUMMY DELICACY");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label addressLabel = new Label("Thibella , Maseru");
        addressLabel.setFont(Font.font("Arial", 11));
        addressLabel.setTextFill(Color.GREY);

        Label phoneLabel = new Label("Tel: (266) 62260503");
        phoneLabel.setFont(Font.font("Arial", 11));
        phoneLabel.setTextFill(Color.GREY);

        Separator sep1 = new Separator();
        sep1.setPadding(new Insets(5, 0, 5, 0));

        String receiptNo = String.format("RCPT-%05d", System.currentTimeMillis() % 100000);
        Label receiptNoLabel = new Label("Receipt #: " + receiptNo);
        receiptNoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Label dateLabel = new Label("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dateLabel.setFont(Font.font("Arial", 11));

        Label serverLabel = new Label("Server: " + currentUser);
        serverLabel.setFont(Font.font("Arial", 11));

        Label customerLabel = new Label("Customer: " + clientName);
        customerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Separator sep2 = new Separator();
        sep2.setPadding(new Insets(5, 0, 5, 0));

        GridPane itemsHeader = new GridPane();
        itemsHeader.setHgap(150);
        itemsHeader.setAlignment(Pos.CENTER);

        Label itemHeader = new Label("Item");
        itemHeader.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Label priceHeader = new Label("Price");
        priceHeader.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        itemsHeader.add(itemHeader, 0, 0);
        itemsHeader.add(priceHeader, 1, 0);

        receiptLayout.getChildren().addAll(titleLabel, addressLabel, phoneLabel, sep1,
                receiptNoLabel, dateLabel, serverLabel, customerLabel, sep2, itemsHeader);

        for (OrderItemDisplay item : orderItems) {
            GridPane itemRow = new GridPane();
            itemRow.setHgap(150);
            itemRow.setAlignment(Pos.CENTER);

            String displayName = item.name();
            if (displayName.contains("(")) {
                displayName = displayName.substring(0, displayName.indexOf("(")).trim();
            }

            Label itemName = new Label(displayName);
            itemName.setFont(Font.font("Arial", 11));

            Label itemPrice = new Label("M" + String.format("%.2f", item.price()));
            itemPrice.setFont(Font.font("Arial", 11));

            itemRow.add(itemName, 0, 0);
            itemRow.add(itemPrice, 1, 0);
            receiptLayout.getChildren().add(itemRow);
        }

        Separator sep3 = new Separator();
        sep3.setPadding(new Insets(5, 0, 5, 0));
        receiptLayout.getChildren().add(sep3);

        GridPane totals = new GridPane();
        totals.setHgap(150);
        totals.setAlignment(Pos.CENTER);

        Label subtotalLabel = new Label("Subtotal:");
        subtotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        Label subtotalValue = new Label("M" + String.format("%.2f", totalAmount));
        subtotalValue.setFont(Font.font("Arial", 11));
        totals.add(subtotalLabel, 0, 0);
        totals.add(subtotalValue, 1, 0);

        Label cashLabel = new Label("Cash:");
        cashLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        Label cashValue = new Label("M" + String.format("%.2f", cashTendered));
        cashValue.setFont(Font.font("Arial", 11));
        totals.add(cashLabel, 0, 1);
        totals.add(cashValue, 1, 1);

        Label changeLabel = new Label("Change:");
        changeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        Label changeValue = new Label("M" + String.format("%.2f", changeGiven));
        changeValue.setFont(Font.font("Arial", 11));
        totals.add(changeLabel, 0, 2);
        totals.add(changeValue, 1, 2);

        receiptLayout.getChildren().add(totals);

        Separator sep4 = new Separator();
        sep4.setPadding(new Insets(5, 0, 5, 0));
        receiptLayout.getChildren().add(sep4);

        Label thankYouLabel = new Label("THANK YOU FOR DINING WITH US!");
        thankYouLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        thankYouLabel.setTextFill(Color.web("#27ae60"));

        Label comeAgainLabel = new Label("PLEASE COME AGAIN!");
        comeAgainLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        comeAgainLabel.setTextFill(Color.web("#e74c3c"));

        receiptLayout.getChildren().addAll(thankYouLabel, comeAgainLabel);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button printBtn = new Button("🖨️ Print Receipt");
        printBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");
        printBtn.setOnAction(_ -> {
            saveToTextFile(clientName);
            showAlert("Print", "Receipt sent to printer (simulated)");
        });

        Button closeBtn = new Button("✖ Close");
        closeBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand;");
        closeBtn.setOnAction(_ -> receiptStage.close());

        buttonBox.getChildren().addAll(printBtn, closeBtn);
        receiptLayout.getChildren().add(buttonBox);

        ScrollPane scrollPane = new ScrollPane(receiptLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");

        Scene scene = new Scene(scrollPane, 400, 600);
        receiptStage.setScene(scene);
        receiptStage.show();
    }

    private void completeOrder() {
        if (orderItems.isEmpty()) {
            showAlert("Error", "No items in order");
            return;
        }

        String clientName = clientField.getText().trim();
        if (clientName.isEmpty()) {
            showAlert("Error", "Please enter client name");
            return;
        }

        if (cashField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter amount tendered");
            return;
        }

        try {
            cashTendered = Double.parseDouble(cashField.getText());
            if (cashTendered < totalAmount) {
                showAlert("Error", "Insufficient amount tendered");
                return;
            }
            changeGiven = cashTendered - totalAmount;
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid cash amount");
            return;
        }

        clientLabel.setText("✓ Client: " + clientName);
        changeLabel.setText("M" + String.format("%.2f", changeGiven));

        saveOrderToDatabase(clientName);
        saveToTextFile(clientName);
        showReceipt(clientName);
        resetSystem();
    }

    private void saveToTextFile(String clientName) {
        try (PrintWriter out = new PrintWriter(new FileWriter("orders.txt", true))) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println("=".repeat(60));
            out.println("PABLW'S YUMMY DELICACY");
            out.println("=".repeat(60));
            out.println("SERVER: " + currentUser);
            out.println("CLIENT: " + clientName);
            out.println("DATE & TIME: " + time);
            out.println("-".repeat(60));
            out.println("ITEMS ORDERED:");
            for (OrderItemDisplay item : orderItems) {
                out.println("  " + item);
            }
            out.println("-".repeat(60));
            out.printf("TOTAL AMOUNT: M%.2f%n", totalAmount);
            out.printf("CASH TENDERED: M%.2f%n", cashTendered);
            out.printf("CHANGE GIVEN: M%.2f%n", changeGiven);
            out.println("=".repeat(60));
            out.println("THANK YOU FOR DINING WITH US!");
            out.println("=".repeat(60) + "\n");
        } catch (IOException ex) {
            showAlert("Error", "Could not save to text file");
        }
    }

    private void startTimeUpdate() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            timeLabel.setText("🕐 " + time);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private record OrderItemDisplay(String category, String name, double price) {
        @Override
        public String toString() {
            return category + " - " + name + " - M" + String.format("%.2f", price);
        }
    }
}