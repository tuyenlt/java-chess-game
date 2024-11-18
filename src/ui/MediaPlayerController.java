package ui;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MediaPlayerController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String name = "";

    @FXML
    private TextField nameTextField;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Label greetingLabel;

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
    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        // Tải tệp FXML mới
        root = FXMLLoader.load(getClass().getResource("./fxml" + fxmlFile));

        // Lấy đối tượng Stage từ sự kiện
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tạo Scene mới từ root FXML và đặt vào Stage
        scene = new Scene(root);
        stage.setScene(scene);

        // Hiển thị Scene mới
        stage.show();
    }
    public void logInFormController(ActionEvent event) throws IOException {
        if(AppState.isValidNameUser()) {
        switchScene(event, "logInScene.fxml");}
        else {
            isValidNameUserLabel.setText("Please Register First!");
        }
    }
    public void registerFormController(ActionEvent event) throws IOException {
        switchScene(event, "registerScene.fxml");
    }
    public void onlineModeMenu(ActionEvent event) throws IOException {
        switchScene(event, "onlineModeScene.fxml");
    }
    public void offlineModeMenu(ActionEvent event) throws IOException {
        switchScene(event, "offlineModeScene.fxml");
    }
    public void returnToMainMenu(ActionEvent event) throws IOException {
        switchScene(event, "mainScene.fxml");
    }
    public void submitName(ActionEvent event) {
        checkValidName();
    }
    public void checkValidName() {
        // Xử lý khi nhấn Enter trên TextField
        name = nameTextField.getText().trim();
        if (name.isEmpty() || !name.matches("[a-zA-Z0-9]+")) {
            greetingLabel.setText("Name is invalid!");
        }
        else {
            AppState.setValidNameUser(true);
            greetingLabel.setText("Hello " + name);

        }
        nameTextField.clear();
    }
}
