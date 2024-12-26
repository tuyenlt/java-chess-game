package chessgame.ui;


import chessgame.network.packets.GeneralPackets.LoginRequest;
import chessgame.utils.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class LoginController implements Initializable {
    @FXML
    Label notifyLabelLogin;
    @FXML
    private TextField usernameTextFieldLogin;
    @FXML
    private TextField passwordTextFieldLogin;
    @FXML
    private AnchorPane logInForm;
    @FXML
    private AnchorPane loadingPane;

    private Consumer<LoginRequest> onSubmit;
    private Runnable onSwitchToRegister;
    private Runnable onReturn;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        notifyLabelLogin.setText("");
        System.out.println("LoginController initialized" + arg0);
        if(logInForm != null) {
            AnimationUtils.applyEffect(logInForm, 0.2);
        }
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
        if (passwordValidate != "ok") {
            notifyLabelLogin.setText(passwordValidate);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/loadingIcon.fxml"));
            Parent loadingListRoot = loader.load();
            LoadingController loadingController = loader.getController();

            loadingPane.getChildren().setAll(loadingListRoot);
            loadingPane.setVisible(true);
            loadingController.loadingLabel.setText("Logging in, please wait...");
            loadingController.cancelFindingButton.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        onSubmit.accept(new LoginRequest(username, password));
    }

    public void registerFormController() {
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

    public void hideLoadingPane() {
        loadingPane.setVisible(false);
    }
}
