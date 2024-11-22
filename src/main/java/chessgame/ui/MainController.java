package chessgame.ui;

import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MainController implements ClientResponseHandle {
    private static Stage stage;
    private Scene scene;
    private Parent root;
    private Parent overlay;

    private User user;
    private static ClientNetwork client;

    private double angle = 0; // Độ dài vòng cung
    private double rotation = 0; // Góc xoay


    private String usernameRegister = "";
    private String passwordRegister = "";


    @FXML
    private VBox playerList;
    @FXML
    private TextField usernameTextFieldRegister;
    @FXML
    private TextField passwordTextFieldRegister;
    @FXML
    private TextField warningTextFieldRegister;
    @FXML
    private Label warningLabel;


    private String usernameLogin = "";
    private String passwordLogin = "";

    @FXML
    private TextField usernameTextFieldLogin;
    @FXML
    private TextField passwordTextFieldLogin;

    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label greetingLabelOnline;
    @FXML
    private Label greetingLabelOffline;
    @FXML
    private Label greetingLabelLogin;
    @FXML
    private Label greetingLabelMain;
    @FXML
    private Label isValidNameUserLabel;


    @FXML
    private AnchorPane secondaryAnchorPane;

    @FXML
    private AnchorPane logInForm;

    @FXML
    private AnchorPane registerForm;
    @FXML
    private AnchorPane onlineModeForm;

    @FXML
    private AnchorPane loadingAnchorPane;


    @FXML
    private Label usernameDisplayLabel;
    @FXML
    private Label eloDisplayLabel;

    public static void setClient(ClientNetwork clientNetwork) {
        client = clientNetwork;
    }

    public static void setStage(Stage newStage) {
        stage = newStage;
    }

    public void initialize() {
        if (secondaryAnchorPane != null && !AppState.isSecondaryPaneOpened()) {
            AppState.setSecondaryPaneOpened(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);

            ScaleTransition scaleTransition = new ScaleTransition();

            // Set thời gian chuyển đổi
            scaleTransition.setDuration(Duration.seconds(1.5)); // Thời gian là 1 giây
            scaleTransition.setNode(secondaryAnchorPane); // Áp dụng cho anchorPane

            // Bắt đầu scale từ 50% (0.5) đến 100% (1.0)
            scaleTransition.setFromX(0.8);
            scaleTransition.setFromY(0.8);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);


            secondaryAnchorPane.setScaleX(0.5);
            secondaryAnchorPane.setScaleY(0.5);

            ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
            parallelTransition.play();

        }
        try {
            FXMLLoader overlayLoader = new FXMLLoader(getClass().getResource("/chessgame/loadingIcon.fxml"));
            overlay = overlayLoader.load();
            overlay.setVisible(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOut(ActionEvent event) {
        AppState.setSecondaryPaneOpened(false);
        usernameLogin = "";
        passwordLogin = "";
        user = null;
        switchScene("mainScene.fxml");
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void switchScene(String fxmlFile) {
        try {

            // Tải tệp FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            Parent root = loader.load();

            if (AppState.isSuccessfulRegistered()) {
                AnchorPane registerForm = new AnchorPane();
                registerForm.setLayoutX(440.0);
                registerForm.setLayoutY(60.0);
                registerForm.setPrefSize(400, 600);
                registerForm.setOpacity(0.0);

                // TODO deo hieu sao khong setlayout cho label duoc

                // Tạo Label
                Label notifyRegisterSuccessLabel = new Label("Register successfully! Redirecting to login...");
                notifyRegisterSuccessLabel.setLayoutX(25.0);
                notifyRegisterSuccessLabel.setLayoutY(500);
                notifyRegisterSuccessLabel.setPrefWidth(350.0);
                notifyRegisterSuccessLabel.setPrefHeight(32.0);
                notifyRegisterSuccessLabel.setStyle("-fx-text-fill: green; -fx-font-size: 15;");
                notifyRegisterSuccessLabel.setWrapText(true);
                notifyRegisterSuccessLabel.setAlignment(javafx.geometry.Pos.CENTER);

                // Thêm Label vào AnchorPane
                registerForm.getChildren().add(notifyRegisterSuccessLabel);

                // Tạo Root Pane để chứa AnchorPane
                if (root instanceof StackPane) {
                    ((StackPane) root).getChildren().add(registerForm);
                    ((StackPane) root).getChildren().add(notifyRegisterSuccessLabel);

                }
            }

            // Tạo Scene mới từ root FXML và đặt vào Stage
            scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị Scene mới
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println(e.getCause());
        }
    }

    public void switchScene(String startScene, String addScene) {
        try {
            // Nạp scene chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + startScene));
            root = loader.load();
            FXMLLoader overlayLoader = new FXMLLoader(getClass().getResource("/chessgame/" + addScene));
            overlay = overlayLoader.load();
            overlay.setVisible(false);
            if (root instanceof Pane) {
                ((Pane) root).getChildren().add(overlay);  // Thêm overlay vào root

            }

            // Tạo và hiển thị Scene mới
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

//            onlineModeForm.setVisible(false);

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void switchScene(String fxmlFile, String username, String elo) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            Parent root = loader.load();

            MainController newController = loader.getController();
//            newController.setGreetingLabel(label, text);
            if (username.length() > 16) {
                username = username.substring(0, 16) + "...";
            }


            newController.usernameDisplayLabel.setText("Username : " + username);
            newController.eloDisplayLabel.setText("Elo : " + elo);


            scene = new Scene(root);
            stage.setScene(scene);


            stage.show();
            double labelWidth = 1195.0 - newController.usernameDisplayLabel.getWidth();
            if (labelWidth < 1195 - 270) labelWidth = 1195 - 270;

            newController.usernameDisplayLabel.setLayoutX(labelWidth);
            newController.eloDisplayLabel.setLayoutX(labelWidth);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println(e.getStackTrace());
        }
    }


    public void logInFormController(ActionEvent event) {
        switchScene("logInScene.fxml");
    }

    public void registerSubmit(ActionEvent event) {
        String userName = usernameTextFieldRegister.getText().trim();
        String passwd = passwordTextFieldRegister.getText();
        String confirmPasswd = warningTextFieldRegister.getText();
        if (passwd.contains(" ") || passwd.length() < 8) {
            passwordTextFieldRegister.clear();
            warningTextFieldRegister.clear();
            warningLabel.setText("Password must have at least 8 characters.");
        } else if (!passwd.equals(confirmPasswd)) {
            warningLabel.setText("Passwords do not match!");
            warningTextFieldRegister.clear();

        } else {
            client.sendRequest(new RegisterRequest(userName, passwd));


            switchScene("registerScene.fxml", "loadingIcon.fxml");
            overlay.setVisible(true);



        }
    }

    public void onLoginSubmit(ActionEvent event) {
        usernameLogin = usernameTextFieldLogin.getText().trim();
        passwordLogin = passwordTextFieldLogin.getText().trim();

        switchScene("logInScene.fxml", "loadingIcon.fxml");
        overlay.setVisible(true);

        client.sendRequest(new LoginRequest(usernameLogin, passwordLogin));
        usernameTextFieldLogin.clear();
        passwordTextFieldLogin.clear();
    }

    public void registerFormController(ActionEvent event) {
        switchScene("registerScene.fxml");
    }


    public void offlineModeMenu(ActionEvent event) {
        switchScene("offlineModeScene.fxml");
    }

    public void returnToMainMenu(ActionEvent event) {
        switchScene("mainScene.fxml");
    }

    public void singlePlayerMode(ActionEvent event){
        switchScene("singlePlayerScene.fxml");
    }
    public void twoPlayerMode(ActionEvent event){
        switchScene("twoPlayerScene.fxml");
    }


    // xử lý dữ liệu server trả về

    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleLoginResponse(LoginResponse response) {
        if (!response.isSuccess) {
            Platform.runLater(() -> {
                showPopup(response.message);
            });
            System.out.println(response.message);
            return;
        }
        user = new User(response.userId, response.userName, response.elo, response.win, response.lose, response.draw);

        new Thread(() -> {
            try {

                Thread.sleep(1000);
                Platform.runLater(() -> {

                    switchScene("onlineModeScene.fxml", user.name, String.valueOf(user.elo));
                    overlay.setVisible(false);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void handleProfileView(ProfileViewResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleRankingList(RankingListResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleRegisterResponse(RegisterResponse response) {
        if (!response.isSuccess) {
            showPopup(response.message);
            return;
        }
        System.out.println(response.message);
        Platform.runLater(() -> {
            overlay.setVisible(false);
            AppState.setSuccessfulRegistered(true);
            switchScene("registerScene.fxml");
        });
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Chờ 3 giây
                Platform.runLater(() -> {
                    AppState.setSuccessfulRegistered(false);
                    switchScene("loginScene.fxml");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void showPopup(String msg) { // TODO làm 1 cái popup thật đẹp ở đây
        System.out.println(msg);
    }

    @FXML
    private ImageView imageView;

    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Hiển thị cửa sổ chọn file và lấy file đã chọn
        File file = fileChooser.showOpenDialog((Stage) imageView.getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());

            
            // Lấy min(width, height) và crop ảnh
            Image croppedImage = cropToSquare(image);

            // Hiển thị ảnh đã cắt
            imageView.setImage(croppedImage);

            Circle clip = new Circle(35.5, 35.5, 35.5); // Đặt bán kính vòng tròn (vì chiều rộng và cao của imageView là 88px)
            imageView.setClip(clip);

            // Thiết lập viền và nền
//            imageView.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 5; -fx-background-color: white;");
        }
    }


    private Image cropToSquare(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();

        // Xác định kích thước vuông (min của width và height)
        double size = Math.min(width, height);

        // Xác định tọa độ để crop ảnh chính giữa
        double x = (width - size) / 2;
        double y = (height - size) / 2;

        // Tạo ảnh mới với kích thước vuông
        return new WritableImage(image.getPixelReader(), (int) x, (int) y, (int) size, (int) size);
    }

     // Liên kết với VBox trong FXML

    public void handleRankingList() {


        // Thêm 30 người chơi vào danh sách
        for (int i = 1; i <= 30; i++) {
            HBox playerRow = new HBox(0); // Khoảng cách giữa các cột
//            playerRow.setStyle("-fx-alignment: CENTER_LEFT;");

            // Cột Rank
            Label rankLabel = new Label("#" + i);
            rankLabel.setPrefWidth(80);
            rankLabel.setStyle("-fx-font-size: 14px;");
            rankLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Cột Player
            Label playerLabel = new Label("Player" + i);
            playerLabel.setPrefWidth(500);
            playerLabel.setStyle("-fx-font-size: 14px;");
            playerLabel.setAlignment(Pos.CENTER_LEFT);

            // Cột ELO
            Label eloLabel = new Label(String.valueOf(1000 + i * 10));
            eloLabel.setPrefWidth(150);
            eloLabel.setStyle("-fx-font-size: 14px;");
            eloLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Thêm các cột vào dòng
            playerRow.getChildren().addAll(rankLabel, playerLabel, eloLabel);

            // Đường phân cách
            Line line = new Line();
            line.setStartX(10); // Khoảng cách từ trái (dịch vào 10px)
            line.setEndX(830 - 10); // Khoảng cách từ phải (dịch vào 10px)
            line.setStartY(0); // Vị trí dọc (giữ nguyên)
            line.setEndY(0);
            line.setStroke(Color.GREEN);
            line.setStrokeWidth(1);

            // Thêm dòng vào danh sách
            VBox playerBox = new VBox(5);
            playerBox.getChildren().addAll(playerRow, line);

            playerList.getChildren().add(playerBox);

        }
        switchScene("onlineModeScene.fxml");

        overlay.setVisible(true);
    }

}

