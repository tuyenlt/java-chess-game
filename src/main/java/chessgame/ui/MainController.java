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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    LoadingController loadingController;
    MainController Controller;

    private User user;
    private static ClientNetwork client;

    private String currentUsername;


    private String usernameRegister = "";
    private String passwordRegister = "";
    private String currentElo;
    @FXML
    private Canvas loadingCanvas;
    @FXML
    private ImageView triangle;
    @FXML
    private ImageView boardImageView;
    @FXML
    private ScrollPane rankingScrollPane;
    @FXML
    private Label notifyLabelLogin;
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
    private AnchorPane secondaryAnchorPane;
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
    @FXML
    private Label notifyLabelRegister;

    public void initialize() {
        if (secondaryAnchorPane != null && !AppState.isSecondaryPaneOpened()) {
            AppState.setSecondaryPaneOpened(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);

            ScaleTransition scaleTransition = new ScaleTransition();

            scaleTransition.setDuration(Duration.seconds(1.5)); // Thời gian là 1 giây
            scaleTransition.setNode(secondaryAnchorPane); // Áp dụng cho anchorPane
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
            FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/chessgame/loadingIcon.fxml"));
            root = loadingLoader.load();
            loadingController = loadingLoader.getController();
            loadingController.loadingAnchorPane.setVisible(false);

//            testController.test.setVisible(false);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void logOut(ActionEvent event) {
        AppState.setSecondaryPaneOpened(false);
        usernameLogin = "";
        passwordLogin = "";
//        currentUsername = "";
//        currentElo = "";
        user = null;
        switchScene("mainScene.fxml");
    }

    public void switchScene(String fxmlFile) {
        try {

            // Tải tệp FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            root = loader.load();
            Controller = loader.getController();
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

    public void switchScene(String startScene, String addScene, double x, double y, double width, double height) {
        try {
            // Nạp scene chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + startScene));
            root = loader.load();
            Controller = loader.getController();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            FXMLLoader addLoader = new FXMLLoader(getClass().getResource("/chessgame/" + addScene));
            Parent loadRoot = addLoader.load();
            loadingController = addLoader.getController();


            loadingController.loadingAnchorPane.setLayoutX(x);
            loadingController.loadingAnchorPane.setLayoutY(y);
            loadingController.loadingAnchorPane.setPrefWidth(width);
            loadingController.loadingAnchorPane.setPrefHeight(height);

            loadingController.loadingAnchorPane = (AnchorPane) loadRoot.lookup("#loadingAnchorPane");
            AnchorPane.setTopAnchor(loadingController.loadingAnchorPane, (720 - loadingController.loadingAnchorPane.getPrefHeight()) / 2);
            AnchorPane.setLeftAnchor(loadingController.loadingAnchorPane, (1280 - loadingController.loadingAnchorPane.getPrefWidth()) / 2);
            AnchorPane.setRightAnchor(loadingController.loadingAnchorPane, null);
            AnchorPane.setBottomAnchor(loadingController.loadingAnchorPane, null);

            if (root instanceof Pane){
                ((Pane) root).getChildren().add(loadingController.loadingAnchorPane);
            }
            loadingController.loadingAnchorPane.setVisible(false);

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void switchScene(String fxmlFile, String username, String elo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            root = loader.load();
            Controller = loader.getController();
//            Controller.setGreetingLabel(label, text);
            if (username.length() > 16) {
                username = username.substring(0, 16) + "...";
            }
            Controller.usernameDisplayLabel.setText("Username : " + username);
            Controller.eloDisplayLabel.setText("Elo : " + elo);

            Controller.rankingScrollPane.setVisible(false);
            Controller.triangle.setVisible(false);

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            double labelWidth = 1195.0 - Controller.usernameDisplayLabel.getWidth();
            if (labelWidth < 1195 - 270) labelWidth = 1195 - 270;

            Controller.usernameDisplayLabel.setLayoutX(labelWidth);
            Controller.eloDisplayLabel.setLayoutX(labelWidth);
        } catch (IOException e) {
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
            switchScene("registerScene.fxml", "loadingIcon.fxml", 440.0, 60.0, 600, 400);
            loadingController.loadingAnchorPane.setVisible(true);
        }
    }

    public void onLoginSubmit(ActionEvent event) {
        usernameLogin = usernameTextFieldLogin.getText().trim();
        passwordLogin = passwordTextFieldLogin.getText().trim();

        switchScene("logInScene.fxml", "loadingIcon.fxml", 440.0, 60.0, 600, 400);
        loadingController.loadingAnchorPane.setVisible(true);

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

    public void showRankingList(ActionEvent event){
        client.sendRequest(new RankingListRequest("a"));
    }


    // xử lý dữ liệu server trả về

    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        // TODO Auto-generated method stub

    }

    public void wrongLogin(String fxmlFile) {
        try {
            // Tạo FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            // Load FXML và lấy controller
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Lấy controller sau khi load
            MainController loginController = loader.getController();

            // Thao tác với notifyLabelLogin từ controller mới
            Platform.runLater(() -> {
                loginController.notifyLabelLogin.setText("Wrong username or password!");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rightRegister(String fxmlFile) {
        try {
            // Tạo FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Lấy controller sau khi load
            MainController registerController = loader.getController();

            // Thao tác với notifyLabelLogin từ controller mới
            Platform.runLater(() -> {
                registerController.notifyLabelRegister.setText("Register successfully!, Redirecting to login in 3s...");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleLoginResponse(LoginResponse response) {
        if (!response.isSuccess) {
            Platform.runLater(() -> {
                wrongLogin("logInScene.fxml");
            });
            return;
        }
        user = new User(response.userId, response.userName, response.elo, response.win, response.lose, response.draw);
        currentUsername = user.name;
        currentElo = String.valueOf(user.elo);
        new Thread(() -> {
            try {
                // Giả lập tải dữ liệu hoặc đợi trong 2 giây
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Sau khi chờ xong, cập nhật giao diện qua Platform.runLater
            Platform.runLater(() -> {
                switchScene("onlineModeScene.fxml", user.name, String.valueOf(user.elo));
                loadingController.loadingAnchorPane.setVisible(false);
            });
        }).start();

    }
    @Override
    public void handleProfileView(ProfileViewResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleRankingList(RankingListResponse response) {
        // TODO Auto-generated method stub
        for(UserRank rank : response.rankingList){
            System.out.println(rank.userName + " " + rank.elo);
        }
    }

    @Override
    public void handleRegisterResponse(RegisterResponse response) {
        if (!response.isSuccess) {
            System.out.println(response.message);
            return;
        }
        Platform.runLater(() -> {
            loadingController.loadingAnchorPane.setVisible(false);
            AppState.setSuccessfulRegistered(true);
            rightRegister("registerScene.fxml");
        });
                Platform.runLater(() -> {
                    AppState.setSuccessfulRegistered(false);
                    switchScene("loginScene.fxml");
                });

    }
    
    

    @Override
    public void handleNewGameResonse(FindGameResponse response) {
        // TODO Auto-generated method stub
        
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
        try {
            // Tạo FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/onlineModeScene.fxml"));


            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Lấy controller sau khi load
            Controller = loader.getController();
        for (int i = 1; i <= 30; i++) {
            HBox playerRow = new HBox(0); // Khoảng cách giữa các cột
            playerRow.setPrefWidth(830);
            playerRow.setAlignment(Pos.CENTER_LEFT); // Căn thẳng hàng với criteria


            // Cột Rank
            Label rankLabel = new Label("#" + i);
            rankLabel.setPrefWidth(70);
            rankLabel.setStyle("-fx-font-size: 14px;");
            rankLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Cột Player
            Label playerLabel = new Label("Player" + i);
            playerLabel.setPrefWidth(500);
            playerLabel.setStyle("-fx-font-size: 14px;");
            playerLabel.setAlignment(Pos.CENTER_LEFT);

            // Cột ELO
            Label eloLabel = new Label(String.valueOf(1000 + 10 * (30 - i)));
            eloLabel.setPrefWidth(150);
            eloLabel.setStyle("-fx-font-size: 14px;");
            eloLabel.setAlignment(javafx.geometry.Pos.CENTER);
            if (i == 1) {
                playerRow.getStyleClass().add("top-1");
                rankLabel.getStyleClass().add("label-top-1");
                eloLabel.getStyleClass().add("label-top-1");
                playerLabel.getStyleClass().add("label-top-1");
            } else if (i == 2) {
                playerRow.getStyleClass().add("top-2");
                rankLabel.getStyleClass().add("label-top-2");
                eloLabel.getStyleClass().add("label-top-2");
                playerLabel.getStyleClass().add("label-top-2");
            } else if (i == 3) {
                playerRow.getStyleClass().add("top-3");
                rankLabel.getStyleClass().add("label-top-3");
                eloLabel.getStyleClass().add("label-top-3");
                playerLabel.getStyleClass().add("label-top-3");
            }
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
            Controller.playerList.getChildren().add(playerBox);
        }
            Platform.runLater(() -> {
                Controller.rankingScrollPane.setVisible(true);
                Controller.triangle.setVisible(true);
                Controller.triangle.setLayoutX(337.0);
                Controller.rankingScrollPane.setPrefWidth(830);
                Controller.boardImageView.setVisible(false);
                Controller.playerList.setPrefWidth(830);
                System.out.println(usernameLogin);
                Controller.usernameDisplayLabel.setText("Username : " + usernameLogin);
                System.out.println(usernameLogin);
                Controller.eloDisplayLabel.setText("Elo : " + currentElo);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFindOnlineGame() {
        switchScene("onlineModeScene.fxml", "loadingIcon.fxml", 450, 88, 632, 830);
        if (root instanceof Pane){
            ((Pane) root).getChildren().add(Controller.rankingScrollPane);
        }
        if( Controller.rankingScrollPane != null) Controller.rankingScrollPane.setVisible(false);
        if( Controller.boardImageView != null) Controller.boardImageView.setVisible(true);
        if( Controller.triangle != null) Controller.triangle.setVisible(false);
        if( loadingController.loadingAnchorPane != null) loadingController.loadingAnchorPane.setVisible(true);

    }
    public void handleEscapeButton(){
        loadingController.loadingAnchorPane.setVisible(false);
        switchScene("onlineModeScene.fxml", "loadingIcon.fxml", 450, 88, 632, 830);
        Controller.rankingScrollPane.setVisible(false);
        Controller.boardImageView.setVisible(true);
        Controller.triangle.setVisible(false);
    }
}