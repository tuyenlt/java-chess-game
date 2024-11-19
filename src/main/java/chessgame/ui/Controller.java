package chessgame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

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




    public void initialize() {



        if (secondaryAnchorPane != null && !AppState.isSecondaryPaneOpened()) {
            AppState.setSecondaryPaneOpened(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        }

    }

    public void logOut(ActionEvent event) throws IOException {
        AppState.setSecondaryPaneOpened(false);
        usernameLogin = "";
        passwordLogin = "";
        switchScene(event, "mainScene.fxml");
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        // Tải tệp FXML mới
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
        Parent root = loader.load();


        // Lấy đối tượng Stage từ sự kiện
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tạo Scene mới từ root FXML và đặt vào Stage
        scene = new Scene(root);
        stage.setScene(scene);

        // Hiển thị Scene mới
        stage.show();
    }
    public void switchScene(ActionEvent event, String fxmlFile, Label label) throws IOException {
        // Tải tệp FXML mới
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
        Parent root = loader.load();

        Controller newController = loader.getController();
        newController.setGreetingLabel(label);

        // Lấy đối tượng Stage từ sự kiện
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tạo Scene mới từ root FXML và đặt vào Stage
        scene = new Scene(root);
        stage.setScene(scene);

        // Hiển thị Scene mới
        stage.show();
    }
    public void logInFormController(ActionEvent event) throws IOException {

            switchScene(event, "logInScene.fxml", greetingLabelLogin);

    }
    public void registerFormController(ActionEvent event) throws IOException {
        switchScene(event, "registerScene.fxml");
    }
    public void onlineModeMenu(ActionEvent event) throws IOException {
        switchScene(event, "onlineModeScene.fxml", greetingLabelOnline);

    }
    public void offlineModeMenu(ActionEvent event) throws IOException {
        switchScene(event, "offlineModeScene.fxml", greetingLabelOffline);
    }
    public void returnToMainMenu(ActionEvent event) throws IOException {
        switchScene(event, "mainScene.fxml", greetingLabelMain);
    }

    public void submitUsernameRegister(ActionEvent event) {
        usernameRegister = usernameTextFieldRegister.getText().trim();
//        checkValidName();
        usernameTextFieldRegister.clear();
    }
    public void submitPasswordRegister(ActionEvent event) {
        passwordRegister = passwordTextFieldRegister.getText().trim();
        passwordTextFieldRegister.clear();
    }
    public void submitUsernameLogin(ActionEvent event) {
        usernameLogin = usernameTextFieldLogin.getText().trim();
//        checkValidName();
        usernameTextFieldLogin.clear();
    }
    public void submitPasswordLogin(ActionEvent event) {
        passwordLogin = passwordTextFieldLogin.getText().trim();
        passwordTextFieldLogin.clear();
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

}
