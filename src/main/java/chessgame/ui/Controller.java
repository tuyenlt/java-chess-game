package chessgame.ui;

import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button; 
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;

public class Controller implements ClientResponseHandle{
    private static Stage stage;
    private Scene scene;
    private Parent root;

    private User user; 
    private static ClientNetwork client = null;


    private String usernameRegister = "";
    private String passwordRegister = "";

    @FXML
    private TextField usernameTextFieldRegister;
    @FXML
    private TextField passwordTextFieldRegister;

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



    public static void setClient(ClientNetwork clientNetwork){
        client = clientNetwork;
    }

    public static void setStage(Stage newStage){
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

    public void logOut(ActionEvent event){
        AppState.setSecondaryPaneOpened(false);
        usernameLogin = "";
        passwordLogin = "";
        user = null;
        switchScene("mainScene.fxml");
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void switchScene(String fxmlFile){
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

    public void switchScene(String fxmlFile, Label label){
        try {
            // Tải tệp FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            Parent root = loader.load();

            Controller newController = loader.getController();
            newController.setGreetingLabel(label);
            // Tạo Scene mới từ root FXML và đặt vào Stage
            scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị Scene mới
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void logInFormController(ActionEvent event){
        switchScene("logInScene.fxml", greetingLabelLogin);
    }


    public void toLogInFormController(ActionEvent event){
        usernameRegister = usernameTextFieldRegister.getText().trim();
        usernameTextFieldRegister.clear();
        passwordRegister = passwordTextFieldRegister.getText().trim();
        passwordTextFieldRegister.clear();
        switchScene("logInScene.fxml", greetingLabelLogin);

    }

    public void registerSubmit(ActionEvent event){
        String userName = usernameTextFieldRegister.getText().trim();
        String passwd = passwordTextFieldRegister.getText().trim();
        client.sendRequest(new RegisterRequest(userName, passwd));
        System.out.println("register with username:" + userName + ", pass:" + passwd);
    }

    public void registerFormController(ActionEvent event){
        switchScene("registerScene.fxml");
    }

    public void onLoginSubmit(ActionEvent event){
        usernameLogin = usernameTextFieldLogin.getText().trim();
        passwordLogin = passwordTextFieldLogin.getText().trim();

        client.sendRequest(new LoginRequest(usernameLogin, passwordLogin));

        usernameTextFieldLogin.clear();
        passwordTextFieldLogin.clear();
    }
    
    public void offlineModeMenu(ActionEvent event){
        switchScene("offlineModeScene.fxml", greetingLabelOffline);
    }

    public void returnToMainMenu(ActionEvent event){
        switchScene("mainScene.fxml", greetingLabelMain);
    }

    public void twoPlayerMode(ActionEvent event){
        switchScene("gameScene.fxml");
    }



//    public void checkValidName() {
//        if (usernameRegister.isEmpty() || !usernameRegister.matches("[a-zA-Z0-9 ]+")) {
//            greetingLabel.setText("Name is invalid!");
//        }
//        else {
//            AppState.setValidNameUser(true);
//            setGreetingLabel(greetingLabel);
//        }
//    }

    public void setGreetingLabel(Label label) {
        if (label == null) {
            System.out.println("Label is null! Please check if the Label is properly initialized.");
        } else {
            if (AppState.isValidNameUser()) {
                label.setText("Hello " + usernameRegister);
                System.out.println("Greeting label updated.");
            } else {
                System.out.println("User name is not valid.");
            }
        }
    }


    // xử lý dữ liệu server trả về

    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleLoginResponse(LoginResponse response) {
        if(!response.isSuccess){
            Platform.runLater(() -> {
                showPopup(response.message);
            });
            System.out.println(response.message);
            return;
        }
        user = new User(response.userId, response.userName,response.elo, response.win, response.lose, response.draw);
        System.out.println(user);
        Platform.runLater(() -> {
            switchScene("onlineModeScene.fxml");        
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
        if(!response.isSuccess){
            Platform.runLater(() -> {
                showPopup(response.message);
            });
            return;
        }
        System.out.println(response.message);
        Platform.runLater(() -> {
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
}

