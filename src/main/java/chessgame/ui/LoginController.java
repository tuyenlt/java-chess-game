package chessgame.ui;

import chessgame.network.packets.GeneralPackets;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import chessgame.ui.MainController;

import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;

public class LoginController {

    private MainController mainController;
    public LoginController(MainController mainController){
        this.mainController = mainController;
    }

    private LoginInfo loginInfo;

    private static ClientNetwork client;

    @FXML
    private TextField usernameTextFieldLogin;
    @FXML
    private TextField passwordTextFieldLogin;
    public void onLoginSubmit(ActionEvent event){
        loginInfo.setUsernameLogin(usernameTextFieldLogin.getText().trim());
        loginInfo.setPasswordLogin(passwordTextFieldLogin.getText().trim());

        client.sendRequest(new GeneralPackets.LoginRequest(loginInfo.getUsernameLogin(), loginInfo.getPasswordLogin()));

        usernameTextFieldLogin.clear();
        passwordTextFieldLogin.clear();
    }

//    public void handleLoginResponse(LoginResponse response) {
//        if(!response.isSuccess){
//            Platform.runLater(() -> {
//                showPopup(response.message);
//            });
//            System.out.println(response.message);
//            return;
//        }
//        loginInfo.setUser(new User(response.userId, response.userName,response.elo, response.win, response.lose, response.draw)) ;
//        System.out.println(loginInfo.getUser());
//        Platform.runLater(() -> {
//            mainController.switchScene("onlineModeScene.fxml");
//        });
//    }
//
//    public void showPopup(String msg) { // TODO làm 1 cái popup thật đẹp ở đây
//        System.out.println(msg);
//    }
    public void registerFormController(ActionEvent event){
        mainController.switchScene("registerScene.fxml");
    }
    public void returnToMainMenu(){
        mainController.switchScene("mainScene.fxml");
    }

}
