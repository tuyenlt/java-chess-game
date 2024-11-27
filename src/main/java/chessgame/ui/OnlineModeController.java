package chessgame.ui;

import java.io.File;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;

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
    private Label usernameDisplayLabel;
    @FXML
    private Label eloDisplayLabel;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private AnchorPane rankingPane;

    public void initialize() {
        System.out.println("OnlineModeController initialized");
        System.out.println(avatarImageView.isVisible());
    }

    public void handleImageUpload() {
        try {
            File avatarFile = ResourcesHanlder.selectFile(new Stage());
            ImageView newAvatarImageView = ResourcesHanlder.createAvatarView(avatarFile.getPath(), true);
            Platform.runLater(() -> {
                avatarImageView.setImage(newAvatarImageView.getImage());
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
            eloDisplayLabel.setText(String.valueOf("Elo: " + user.elo));
            double labelWidth = 1195.0 - usernameDisplayLabel.getWidth() - 80;
            if (labelWidth < 1195 - 270) labelWidth = 1195 - 270;
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
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleRankingListClose() {
        rankingPane.setVisible(false);
    }

    public void handleFindOnlineGame() {
        client.sendRequest(new FindGameRequest(user.playerId, user.name, user.elo));
        System.out.println("Find online game");
    }

    public void handleEscapeButton() {
        client.sendRequest(new MsgPacket("/cancel-find-game"));
    }

    public void handleRankingListShow() {
        client.sendRequest(new RankingListRequest(user.name));
    }

    public void logOut() {
        onLogout.run();
    }

}