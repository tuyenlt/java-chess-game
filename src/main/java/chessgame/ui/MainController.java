package chessgame.ui;

import chessgame.game.SinglePlayerMode;
import chessgame.game.TwoPlayerOfflineMode;
import chessgame.game.TwoPlayerOnlineMode;
import chessgame.network.ClientNetwork;
import chessgame.network.ClientResponseHandle;
import chessgame.network.GameClient;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import chessgame.network.packets.IngamePackets.InitPacket;
import chessgame.utils.Config;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements ClientResponseHandle, Initializable {
    private static Stage stage;
    private Scene scene;
    private Parent root;

    private static LoginController loginController;
    private static RegisterController registerController;
    private static OnlineModeController onlineModeController;
    
    private static User user;
    
    private static ClientNetwork client;
    
    @FXML
    private AnchorPane loadingPane;

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
        if (secondaryAnchorPane != null) {
            double duration = 0.2;
            AnimationUtils.applyEffect(secondaryAnchorPane, duration);
        }
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void logOut(ActionEvent event) {
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
        if(client == null){
            try {
                client = new ClientNetwork(Config.clientTimeOut, Config.serverTcpPort, Config.serverUdpPort, Config.serverAddress);
                client.connectMainServer();
                client.setUiResponseHandler(this);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(e.getCause());
                System.out.println(e.getMessage());
            }
        }

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
            onlineModeController.getTwoPlayerButton().setOnAction((event) -> {
                twoPlayerMode(event);
            });
            onlineModeController.getSinglePlayerButton().setOnAction((event) -> {
                singlePlayerMode(event);
            });
            onlineModeController.setOnLogout(() -> {
                user = null;
                switchScene("mainScene.fxml");
            });
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
                onlineModeMenu();
            }
        });
        Scene scene = new Scene(game);
        stage.setScene(scene);
        stage.show();
    }

    public void twoPlayerMode(ActionEvent event){
        TwoPlayerOfflineMode game = new TwoPlayerOfflineMode(false);
        game.setOnGameEnd(()->{
            if(user == null){
                switchScene("offlineModeScene.fxml");
            }else{
                onlineModeMenu();
            }
        });
        Scene scene = new Scene(game);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        onlineModeController.handleHistoryShow(response);
    }


    @Override
    public void handleLoginResponse(LoginResponse response) {
        loginController.hideLoadingPane();
        if (!response.isSuccess) {
            Platform.runLater(() -> {
                if(loginController != null){
                    loginController.setNotifyLabelLogin(response.message);
                }
            });
            return;
        }
        user = new User(response.userId, response.userName, response.elo, response.win, response.lose, response.draw);
        new Thread(() -> {
            Platform.runLater(() -> {
                onlineModeMenu();
            });
        }).start();
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
        registerController.hideLoadingPane();
        if (!response.isSuccess) {
            Platform.runLater(() -> {
                registerController.setWarningLabel(response.message);
            });
            return;
        }
        new Thread(() -> {
            Platform.runLater(() -> {
                switchToLogin();
            });
        }).start();


    }

    @Override
    public void handleNewGameResponse(FindGameResponse response) {
        TwoPlayerOnlineMode game;
        if(response.side.equals("w")){
            game = new TwoPlayerOnlineMode(false);
        }else{
            game = new TwoPlayerOnlineMode(true);
        }     
        game.setPlayerBottom(user.name, String.valueOf(user.elo), response.side, true);
        GameClient gameClient = new GameClient(10000, response.tcpPort, response.udpPort, Config.serverAddress);
        gameClient.setResponHandler(game);
        game.setClient(gameClient);
        try{
            gameClient.connectGameServer();       
        }catch(IOException e){
            System.out.println(e);
        }
        gameClient.sendRequest(new InitPacket(user.playerId));

        game.setOnGameEnd((eloChange)->{
            user.elo += eloChange;
            onlineModeMenu();
        });

        game.setOnGameEnd(()->{
            client.sendRequest(new MsgPacket("/surrender"));
            onlineModeMenu();
        });

        Scene scene = new Scene(game);
        Platform.runLater(()->{
            stage.setScene(scene);
        });
    }
}