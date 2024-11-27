package chessgame.ui;

import chessgame.game.HistoryGameReplay;
import chessgame.game.SinglePlayerMode;
import chessgame.game.TwoPlayerOfflineMode;
import chessgame.game.TwoPlayerOnlineMode;
import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.GameNetwork;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import chessgame.network.packets.IngamePackets.InitPacket;
import chessgame.utils.ResourcesHanlder;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

public class MainController implements ClientResponseHandle, Initializable {
    private static Stage stage;
    private Scene scene;
    private Parent root;

    LoadingController loadingController;
    MainController Controller;
    private static LoginController loginController;
    private static RegisterController registerController;
    private static OnlineModeController onlineModeController;

    private static User user;

    private static ClientNetwork client;

    @FXML
    private AnchorPane secondaryAnchorPane;

    public static void setClient(ClientNetwork clientNetwork) {
        client = clientNetwork;
    }

    public static void setStage(Stage newStage) {
        stage = newStage;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {       
        if (secondaryAnchorPane != null && !AppState.isSecondaryPaneOpened()) {
            AppState.setSecondaryPaneOpened(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), secondaryAnchorPane);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);

            ScaleTransition scaleTransition = new ScaleTransition();

            scaleTransition.setDuration(Duration.seconds(1)); 
            scaleTransition.setNode(secondaryAnchorPane);
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
        user = null;
        switchScene("mainScene.fxml");
    }

    public FXMLLoader switchScene(String fxmlFile) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/" + fxmlFile));
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return loader;
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
            System.out.println(e);
        }
        return null;
    }

    public void switchToLogin() {
        FXMLLoader loader = switchScene("logInScene.fxml");  
        loginController = loader.getController();  
        
        loginController.setOnSubmmit((LoginRequest) -> {
            client.sendRequest(LoginRequest);
        });

        loginController.setOnSwitchToRegister(() -> {
            switchToRegister();
        });

        loginController.setOnReturn(() -> {
            switchScene("mainScene.fxml");
        });
    }

    public void switchToRegister() {
        FXMLLoader loader = switchScene("registerScene.fxml");
        registerController = loader.getController();
        
        registerController.setOnSubmit((RegisterRequest) -> {
            client.sendRequest(RegisterRequest);
        });

        registerController.setOnReturn(() -> {
            switchScene("mainScene.fxml");
        });

        registerController.setOnSwitchToLogin(() -> {
            switchToLogin();
        });
    }


    public void offlineModeMenu(ActionEvent event) {
        switchScene("offlineModeScene.fxml");
    }

    public void onlineModeMenu() {
        if (user == null) {
            switchToLogin();
        } else {
            FXMLLoader loader = switchScene("onlineModeScene.fxml");
            onlineModeController = loader.getController();
            onlineModeController.setUserInformation(user);
            onlineModeController.setClient(client);
        }
    }

    public void returnToMainMenu(ActionEvent event) {
        switchScene("mainScene.fxml");
    }

    public void singlePlayerMode(ActionEvent event){
        SinglePlayerMode game = new SinglePlayerMode(false);
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
        // TwoPlayerOfflineMode game = new TwoPlayerOfflineMode(false);
        // game.setOnGameEnd(()->{
        //     if(user == null){
        //         switchScene("offlineModeScene.fxml");
        //     }else{
        //         switchScene("onlineModeScene.fxml");
        //     }
        // });
        // Scene scene = new Scene(game);
        HistoryGameReplay replayGame = new HistoryGameReplay("d2d4 d7d5 e2e4 d5e4 c1f4 g8f6 d1d3 e4d3 c2d3 d8d4 b1c3 d4f4 d3d4 c7c6 b2b3 f6e4 f2f3", true);
        replayGame.setOnReturn(()->{
            switchScene("offlineModeScene.fxml");
        });
        Scene scene = new Scene(replayGame);
        stage.setScene(scene);
        stage.show();
    }

    public void showRankingList(ActionEvent event){
        client.sendRequest(new RankingListRequest("a"));
    }

    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        // TODO Auto-generated method stub

    }
    @Override
    public void handleLoginResponse(LoginResponse response) {
        if (!response.isSuccess) {
            Platform.runLater(() -> {
                if(loginController != null){
                    loginController.setNotifyLabelLogin(response.message);
                }
            });
            return;
        }
        user = new User(response.userId, response.userName, response.elo, response.win, response.lose, response.draw);
        Platform.runLater(() -> {
            onlineModeMenu();
            loadingController.loadingStackPane.setVisible(false);
        });

    }
    @Override
    public void handleProfileView(ProfileViewResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleRankingList(RankingListResponse response) {
        onlineModeController.handleRankingList(response);
    }

    @Override
    public void handleRegisterResponse(RegisterResponse response) {
        if (!response.isSuccess) {
            registerController.setWarningLabel(response.message);
            return;
        }
        Platform.runLater(() -> {
            loadingController.loadingStackPane.setVisible(false);
            AppState.setSuccessfulRegistered(true);
        });
        Platform.runLater(() -> {
            AppState.setSuccessfulRegistered(false);
            switchToLogin();
        });

    }

    @Override
    public void handleNewGameResonse(FindGameResponse response) {
        TwoPlayerOnlineMode game;
        if(response.side.equals("w")){
            game = new TwoPlayerOnlineMode(false);
        }else{
            game = new TwoPlayerOnlineMode(true);
        }     
        game.setPlayerBottom(user.name, String.valueOf(user.elo), response.side);
        GameNetwork gameClient = new GameNetwork(10000, response.tcpPort, response.udpPort, "localhost");
        gameClient.setResponHandler(game);
        game.setClient(gameClient);
        try{
            gameClient.connectGameServer();       
        }catch(IOException e){
            System.out.println(e);
        }
        gameClient.sendRequest(new InitPacket(user.playerId));
        game.setOnGameEnd(()->{
            switchScene("onlineModeScene.fxml");
        });
        Scene scene = new Scene(game);
        Platform.runLater(()->{
            stage.setScene(scene);
        });
    }
}