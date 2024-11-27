package chessgame.ui;

import java.io.IOException;
import java.util.function.Consumer;

import chessgame.network.packets.GeneralPackets.RegisterRequest;
import chessgame.utils.Validator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RegisterController {

    @FXML
    private AnchorPane loadingPane;

    @FXML
    private TextField usernameTextFieldRegister;

    @FXML
    private TextField passwordTextFieldRegister;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Label warningLabel;

    @FXML
    private AnchorPane registerForm;
    private Runnable onSwitchToLogin;
    private Consumer<RegisterRequest> onSubmit;
    private Runnable onReturn;

    public void initialize() {
        if(registerForm != null) {
            AnimationUtils.applyEffect(registerForm, 0.2);
        }
    }

    public void registerSubmit(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/loadingIcon.fxml"));
            Parent loadingListRoot = loader.load();
            LoadingController loadingListController = loader.getController();

            Platform.runLater(() -> {
                loadingPane.getChildren().setAll(loadingListRoot);
                loadingPane.setVisible(true);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        String username = usernameTextFieldRegister.getText();
        String password = passwordTextFieldRegister.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();

        String userNameValidate = Validator.userNameValidator(username);
        String passwordValidate = Validator.passwordValidator(password);
        String emailValidate = Validator.emailValidator(email);

        if(userNameValidate != "ok") {
            warningLabel.setText(userNameValidate);
            return;
        }

        if(passwordValidate != "ok") {
            warningLabel.setText(passwordValidate);
            return;
        }

        if(!password.equals(confirmPassword)) {
            warningLabel.setText("Password and confirm password must be the same");
            return;
        }

        if (emailValidate != "ok") {
            warningLabel.setText(emailValidate);
            return;
        }

        onSubmit.accept(new RegisterRequest(username, email, password));
    }

    public void toLoginForm() {
        onSwitchToLogin.run();
    }

    public void returnToMainMenu() {
        onReturn.run();
    }

    public void setOnSwitchToLogin(Runnable onSwitchToLogin) {
        this.onSwitchToLogin = onSwitchToLogin;
    }

    public void setOnSubmit(Consumer<RegisterRequest> onSubmit) {
        this.onSubmit = onSubmit;
    }

    public void setOnReturn(Runnable onReturn) {
        this.onReturn = onReturn;
    }

    public void setWarningLabel(String message) {
        warningLabel.setText(message);
    }
}
