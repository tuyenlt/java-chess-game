package chessgame.ui;

import chessgame.game.MainGame;
import chessgame.game.SinglePlayer;
import chessgame.game.TwoPlayer;
import chessgame.game.TwoPlayerOnlineMode;
import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.GameNetwork;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import chessgame.network.packets.IngamePackets.InitPacket;
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

    LoadingController loadingController;
    MainController Controller;

    private static User user;

    private static ClientNetwork client;

    private String usernameRegister = "";
    private String passwordRegister = "";

    @FXML
    private ImageView rankingPic;
    @FXML
    private ImageView avatarImageView;
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
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);

            ScaleTransition scaleTransition = new ScaleTransition();

            scaleTransition.setDuration(Duration.seconds(1)); // Thời gian là 1 giây
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
            loadingController.loadingStackPane.setVisible(false);

            if (user.name.length() > 12) {
                user.name = user.name.substring(0, 12) + "...";
            }

        } catch (Exception e) {
                System.out.println(e.getStackTrace());
        }
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void logOut(ActionEvent event) {
        AppState.setSecondaryPaneOpened(false);
        usernameLogin = "";
        passwordLogin = "";
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
            System.out.println(e.getStackTrace());
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


            loadingController.loadingStackPane.setLayoutX(x);
            loadingController.loadingStackPane.setLayoutY(y);
            loadingController.loadingStackPane.setPrefWidth(width);
            loadingController.loadingStackPane.setPrefHeight(height);
            loadingController.loadingAnchorPane.setLayoutX(x);
            loadingController.loadingAnchorPane.setLayoutY(y);
            loadingController.loadingAnchorPane.setPrefWidth(width);
            loadingController.loadingAnchorPane.setPrefHeight(height);


            if (root instanceof Pane){
                ((Pane) root).getChildren().add(loadingController.loadingStackPane);
            }
            loadingController.loadingStackPane.setVisible(false);

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
            if (user.name.length() > 12) {
                user.name = user.name.substring(0, 12) + "...";
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
            switchScene("registerScene.fxml", "loadingIcon.fxml", 440.0, 60.0, 400, 600);
            if (loadingController.escLabel != null) {
                loadingController.escLabel.setLayoutX(0);
                loadingController.escLabel.setVisible(false);
            }
            if (loadingController.cancelFindingButton != null) {
                loadingController.cancelFindingButton.setVisible(false);
            }
            loadingController.loadingStackPane.setVisible(true);
        }
    }

    public void onLoginSubmit(ActionEvent event) {
        usernameLogin = usernameTextFieldLogin.getText().trim();
        passwordLogin = passwordTextFieldLogin.getText().trim();

        switchScene("logInScene.fxml", "loadingIcon.fxml", 440.0, 60.0, 400, 600);
        if (loadingController.escLabel != null) {
            loadingController.escLabel.setVisible(false);
            loadingController.escLabel.setLayoutX(0);
        }
        if (loadingController.cancelFindingButton != null) {
            loadingController.cancelFindingButton.setVisible(false);
        }
        loadingController.loadingStackPane.setVisible(true);

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
        SinglePlayer game = new SinglePlayer(false);
        game.setOnGameEnd(()->{
            if(user == null){
                switchScene("offlineModeScene.fxml");
            }else{
                switchScene("onlineModeScene.fxml");
            }
        });
        Scene scene = new Scene(game);
        stage.setScene(scene);
        stage.show();
    }
    public void twoPlayerMode(ActionEvent event){
        TwoPlayer game = new TwoPlayer(false);
        game.setOnGameEnd(()->{
            if(user == null){
                switchScene("offlineModeScene.fxml");
            }else{
                switchScene("onlineModeScene.fxml");
            }
        });
        Scene scene = new Scene(game);
        stage.setScene(scene);
        stage.show();
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
//        new Thread(() -> {
//            try {
//
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // Sau khi chờ xong, cập nhật giao diện qua Platform.runLater
            Platform.runLater(() -> {
                switchScene("onlineModeScene.fxml", user.name, String.valueOf(user.elo));
                loadingController.loadingStackPane.setVisible(false);
                StackPane.setAlignment(Controller.rankingPic, Pos.TOP_LEFT);
            });
//        }).start();

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
            loadingController.loadingStackPane.setVisible(false);
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
        TwoPlayerOnlineMode game;
        if(response.side == "w"){
            game = new TwoPlayerOnlineMode(false);
        }else{
            game = new TwoPlayerOnlineMode(true);
        }     
        game.setPlayerBottom(user.name, String.valueOf(user.elo), response.side);
        GameNetwork gameClient = new GameNetwork(10000, response.tcpPort, response.udpPort, "localhost");
        try{
            gameClient.connectGameServer();       
        }catch(IOException e){
            System.out.println(e);
        }
        gameClient.sendRequest(new InitPacket(user.playerId));
        game.setClient(gameClient);
        game.setOnGameEnd(()->{
            switchScene("onlineModeScene.fxml");
        });
        Scene scene = new Scene(game);
        stage.setScene(scene);
    }

    @FXML
    private ImageView imageView;


    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Hiển thị cửa sổ chọn file và lấy file đã chọn
        File file = fileChooser.showOpenDialog((Stage) avatarImageView.getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());


            // Lấy min(width, height) và crop ảnh
            Image croppedImage = cropToSquare(image);

            // Hiển thị ảnh đã cắt
            avatarImageView.setImage(croppedImage);

            Circle clip = new Circle(35.5, 35.5, 35.5); // Đặt bán kính vòng tròn (vì chiều rộng và cao của imageView là 88px)
            avatarImageView.setClip(clip);

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
                StackPane.setAlignment(Controller.rankingPic, Pos.TOP_LEFT);
                double labelWidth = 1195.0 - Controller.usernameDisplayLabel.getWidth();
                if (labelWidth < 1195 - 270) labelWidth = 1195 - 270;

                Controller.usernameDisplayLabel.setLayoutX(labelWidth);
                Controller.eloDisplayLabel.setLayoutX(labelWidth);
                Controller.usernameDisplayLabel.setText("Username : " + user.name);
                Controller.eloDisplayLabel.setText("Elo : " + user.elo);
//                Controller.usernameDisplayLabel.setLayoutX(950);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleFindOnlineGame() {
        switchScene("onlineModeScene.fxml", "loadingIcon.fxml", 648, 88, 632, 632);

        if (root instanceof Pane){
            ((Pane) root).getChildren().add(Controller.rankingScrollPane);
        }

        Controller.rankingScrollPane.setVisible(false);
        Controller.boardImageView.setVisible(true);
        Controller.triangle.setVisible(false);
        loadingController.loadingStackPane.setVisible(true);

        loadingController.loadingCanvas.setLayoutX(291);
        loadingController.loadingCanvas.setLayoutY(291);

        loadingController.escLabel.setVisible(true);

        loadingController.cancelFindingButton.setVisible(false);

        StackPane.setAlignment(Controller.rankingPic, Pos.TOP_LEFT);
    }

    
    public void handleEscapeButton(){
        loadingController.loadingStackPane.setVisible(false);
        switchScene("onlineModeScene.fxml", "loadingIcon.fxml", 648, 88, 632, 632);
        Controller.rankingScrollPane.setVisible(false);
        Controller.boardImageView.setVisible(true);
        Controller.triangle.setVisible(false);
        StackPane.setAlignment(Controller.rankingPic, Pos.TOP_LEFT);
    }
}