package chessgame.ui;

import java.io.File;
import java.io.IOException;
import java.security.cert.PolicyNode;

import chessgame.network.ClientNetwork;
import chessgame.network.User;
import chessgame.network.packets.GeneralPackets.*;
import chessgame.utils.ResourcesHanlder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class OnlineModeController {
    private User user;

    private ClientNetwork client;

    private Runnable onLogout;

    @FXML
    private AnchorPane root;
    @FXML
    private Label usernameDisplayLabel;
    @FXML
    private Label eloDisplayLabel;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private AnchorPane rankingPane;
    @FXML
    private AnchorPane loadingPane;
    @FXML
    private AnchorPane historyPane;
    @FXML
    private ImageView boardImageView;

    public void initialize() throws IOException{
        System.out.println("OnlineModeController initialized");
        System.out.println(avatarImageView.isVisible());
    }

    public void handleImageUpload() {
        try {
            File selectFile = ResourcesHanlder.selectFile(new Stage());
            File avatarFile = ResourcesHanlder.convertToJPG(selectFile);
            if(avatarFile == null) return;
            Image avatarImage = new Image(avatarFile.toURI().toString());
            client.sendImage(avatarFile, user.name + ".jpg");
            Platform.runLater(() -> {
                avatarImageView.setImage(ResourcesHanlder.cropImageToSquare(avatarImage));
                avatarImageView.setFitHeight(70);
                avatarImageView.setFitWidth(70);
                Circle clip = new Circle(35, 35, 35);
                avatarImageView.setClip(clip);
                avatarImageView.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAvatarImage(Image avatarImage) {
        Platform.runLater(() -> {
            avatarImageView.setImage(avatarImage);
            avatarImageView.setFitHeight(70);
            avatarImageView.setFitWidth(70);
            Circle clip = new Circle(35, 35, 35);
            avatarImageView.setClip(clip);
            avatarImageView.setVisible(true);
        });
    }

    public void setUserInformation(User user) {
        this.user = user;
        Platform.runLater(() -> {
            usernameDisplayLabel.setText("User Name: " + user.name);
            eloDisplayLabel.setText("Elo: " + user.elo);
            double labelWidth = 1195.0 - usernameDisplayLabel.getWidth() - 80;
//            if (labelWidth < 1195 - 270) labelWidth = 1195 - 270;
            usernameDisplayLabel.setLayoutX(labelWidth);
            eloDisplayLabel.setLayoutX(labelWidth);
            loadAvatarImage(ResourcesHanlder.getAvatarImage(user.name));
        });
    }

    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    public void setClient(ClientNetwork client) {
        this.client = client;
    }

    public void handleRankingList(RankingListResponse response) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/rankingList.fxml"));
            Parent rankingListRoot = loader.load();
            RankingListController rankingListController = loader.getController();
            rankingListController.updateRankingList(response, user);

            Platform.runLater(() -> {
                rankingPane.getChildren().setAll(rankingListRoot);
                rankingPane.setVisible(true);
                boardImageView.setVisible(false);
                historyPane.setVisible(false);


            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/historyList.fxml"));
            Parent historyListRoot = loader.load();
            HistoryController historyListController = loader.getController();
            historyListController.updateHistory();

            Platform.runLater(() -> {
                historyPane.getChildren().setAll(historyListRoot);
                historyPane.setVisible(true);
                rankingPane.setVisible(false);
                boardImageView.setVisible(false);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFindOnlineGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chessgame/loadingIcon.fxml"));
            Parent loadingRoot = loader.load();
            LoadingController loadingController = loader.getController();
            loadingController.setOnCancel(()->{
                client.sendRequest(new MsgPacket("/cancel-find-game"));
                loadingPane.setVisible(false);
            });

            Platform.runLater(() -> {
                loadingPane.getChildren().setAll(loadingRoot);
                loadingPane.setVisible(true);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.sendRequest(new FindGameRequest(user.playerId, user.name, user.elo));
        System.out.println("Find online game");
    }

    public void handleEscapeButton() {
        client.sendRequest(new MsgPacket("/cancel-find-game"));
        System.out.println("Escape button pressed");
        Platform.runLater(() -> {
            loadingPane.setVisible(false);
            rankingPane.setVisible(false);
            historyPane.setVisible(false);
            // histortyy
        });
    }

    public void handleRankingListShow() {
        client.sendRequest(new RankingListRequest(user.name));
    }



    public void logOut() {
        onLogout.run();
    }
}