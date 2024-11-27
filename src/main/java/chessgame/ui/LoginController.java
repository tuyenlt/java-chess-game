package chessgame.ui;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import chessgame.network.packets.GeneralPackets.LoginRequest;
import chessgame.utils.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameTextFieldLogin;

    @FXML
    private TextField passwordTextFieldLogin;

    @FXML 
    Label notifyLabelLogin;
    
    private Consumer<LoginRequest> onSubmit;
    private Runnable onSwitchToRegister;
    private Runnable onReturn;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        notifyLabelLogin.setText("");
        System.out.println("LoginController initialized" + arg0);
    }

    public void onLoginSubmit() {
        String username = usernameTextFieldLogin.getText();
        String password = passwordTextFieldLogin.getText();
        String userNameValidate = Validator.userNameValidator(username);
        String passwordValidate = Validator.passwordValidator(password);
        if (userNameValidate != "ok") {
            notifyLabelLogin.setText(userNameValidate);
            return;
        }
        if(passwordValidate != "ok") {
            notifyLabelLogin.setText(passwordValidate);
            return;
        }
        onSubmit.accept(new LoginRequest(username, password));
    }

    public void registerFormController(){
        onSwitchToRegister.run();
    }

    public void setNotifyLabelLogin(String message) {
        notifyLabelLogin.setText(message);
    }

    public void setOnSubmmit(Consumer<LoginRequest> onSubmit) {
        this.onSubmit = onSubmit;
    }

    public void setOnSwitchToRegister(Runnable onSwitchToRegister) {
        this.onSwitchToRegister = onSwitchToRegister;
    }

    public void setOnReturn(Runnable onReturn) {
        this.onReturn = onReturn;
    }

    public void returnToMainMenu() {
        onReturn.run();
    }
}
