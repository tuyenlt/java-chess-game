package chessgame.ui;

import java.util.function.Consumer;

import chessgame.network.packets.GeneralPackets.RegisterRequest;
import chessgame.utils.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController {
    
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

    private Runnable onSwitchToLogin;
    private Consumer<RegisterRequest> onSubmit;
    private Runnable onReturn;

    public void registerSubmit(ActionEvent event) {
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

        onSubmit.accept(new RegisterRequest(username, password));
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
