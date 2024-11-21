package chessgame.ui;

import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MainController implements ClientResponseHandle {
    private static Stage stage;
    private Scene scene;
    private Parent root;

    private User user;
    private static ClientNetwork client;
    private Parent overlay;


    private String usernameRegister = "";
    private String passwordRegister = "";

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
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
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

            // Tạo Scene mới từ root FXML và đặt vào Stage
            scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị Scene mới
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void switchScene(String sceneFile, String loadingIcon) {
        try {
            // Nạp scene chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + sceneFile));
            Parent root = loader.load();

            // Nạp overlay
            FXMLLoader overlayLoader = new FXMLLoader(getClass().getResource("/chessgame/" + loadingIcon));
            overlay = overlayLoader.load();
            overlay.setVisible(false);


            // Ép kiểu root và overlay thành Pane để sử dụng getChildren()
            if (root instanceof Pane) {
                ((Pane) root).getChildren().add(overlay);  // Thêm overlay vào root
            }

            // Tạo và hiển thị Scene mới
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void switchScene(String fxmlFile, String username, String elo) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            Parent root = loader.load();

            MainController newController = loader.getController();
//            newController.setGreetingLabel(label, text);

            newController.usernameDisplayLabel.setText("Username : " + username);
            newController.eloDisplayLabel.setText("Elo : " + elo);

            scene = new Scene(root);
            stage.setScene(scene);


            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void logInFormController(ActionEvent event) {
        switchScene("logInScene.fxml", "loadingIcon.fxml");
    }

    public void registerSubmit(ActionEvent event) {
        String userName = usernameTextFieldRegister.getText().trim();
        String passwd = passwordTextFieldRegister.getText();
        String confirmPasswd = warningTextFieldRegister.getText();
        if (passwd.contains(" ") || passwd.isEmpty() || passwd.length() < 8) {
            passwordTextFieldRegister.clear();
            warningTextFieldRegister.clear();
            warningLabel.setText("Password must have at least 8 characters.");
        } else if (!passwd.equals(confirmPasswd)) {
            warningLabel.setText("Passwords do not match!");
            warningTextFieldRegister.clear();

        } else {
        client.sendRequest(new RegisterRequest(userName, passwd));
            if (overlay != null) overlay.setVisible(true);

            System.out.println("register with username:" + userName + ", pass:" + passwd);
        }
    }

    public void onLoginSubmit(ActionEvent event) {
        usernameLogin = usernameTextFieldLogin.getText().trim();
        passwordLogin = passwordTextFieldLogin.getText().trim();

        client.sendRequest(new LoginRequest(usernameLogin, passwordLogin));
        if (overlay != null) overlay.setVisible(true);
        usernameTextFieldLogin.clear();
        passwordTextFieldLogin.clear();
    }

    public void registerFormController(ActionEvent event) {
        switchScene("registerScene.fxml", "loadingIcon.fxml");
    }


    public void offlineModeMenu(ActionEvent event) {
        switchScene("offlineModeScene.fxml");
    }

    public void returnToMainMenu(ActionEvent event) {
        switchScene("mainScene.fxml");
    }

    public void singlePlayerMode(ActionEvent event){
        switchScene("gameScene.fxml");
    }
    public void twoPlayerMode(ActionEvent event){
        switchScene("gameScene.fxml");
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
        System.out.println(user);
        Platform.runLater(() -> {
            System.out.println("switched");
            switchScene("onlineModeScene.fxml", user.name, String.valueOf(user.elo));


        });

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
            switchScene("loginScene.fxml");
        });
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

    /**
     * Hàm cắt ảnh thành hình vuông dựa trên min(width, height)
     */
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
}