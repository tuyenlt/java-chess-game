package chessgame.game;

import chessgame.ui.BoardPane;
import chessgame.ui.GameEndAnnouncement;
import chessgame.ui.PlayerSection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

abstract public class MainGame extends StackPane {
    protected int gameTime = 600;
    protected BoardPane boardPane;
    protected VBox rightSection = new VBox();
    protected boolean isBoardReverse = false;
    protected PlayerSection playerSectionTop;
    protected PlayerSection playerSectionBottom;
    protected PlayerInfo playerTop;
    protected PlayerInfo playerBottom;

    protected class PlayerInfo{
        public String name;
        public String elo;
        public String side;
        public PlayerInfo(String name, String elo, String side) {
            this.name = name;
            this.elo = elo;
            this.side = side;
        }
    }


    public MainGame(String mode, boolean isBoardReverse) {
        getStylesheets().add(getClass().getResource("/chessgame/style.css").toExternalForm());
        this.setPrefSize(1280, 720);
        AnchorPane backgroundPane = new AnchorPane();
        backgroundPane.setPrefSize(1280, 720);

        ImageView backgroundImageView = new ImageView(new Image(getClass().getResourceAsStream("/chessgame/image/resized_image.png")));
        backgroundImageView.setFitWidth(1280);
        backgroundImageView.setFitHeight(720);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setPickOnBounds(true);

        backgroundPane.getChildren().add(backgroundImageView);

        AnchorPane contentPane = new AnchorPane();
        contentPane.setPrefSize(1280, 720);

        rightSection.setLayoutX(720);
        rightSection.setLayoutY(0);
        rightSection.setPrefSize(560, 720);
        rightSection.setStyle("-fx-background-image: url('/chessgame/image/bg.png'); " +
        "-fx-background-size: cover; " +
        "-fx-background-repeat: no-repeat; " +
        "-fx-background-position: center;");

        playerSectionTop = new PlayerSection("Player 1", "???", gameTime, "b");
        playerSectionBottom = new PlayerSection("Player 2", "???", gameTime, "w");
        rightSection.getChildren().addAll(playerSectionTop, playerSectionBottom);

        boardPane = new BoardPane(mode, isBoardReverse);
        boardPane.setLayoutX(0);
        boardPane.setLayoutY(0);
        boardPane.setPrefSize(720, 720);
        boardPane.setOnMovePiece((currentTurn) -> {
            new Thread(() -> {
                if (currentTurn.equals(playerSectionBottom.getSide())) {
                    Platform.runLater(() -> {
                        playerSectionTop.stopTimer();
                        playerSectionBottom.startTimer();
                    });
                } else {
                    Platform.runLater(() -> {
                        playerSectionTop.startTimer();
                        playerSectionBottom.stopTimer();
                    });
                }
                handleOnMovePiece(currentTurn);
                hanldeGameEnd(currentTurn);
            }).start();
        });
        boardPane.start();
        
        contentPane.getChildren().addAll(boardPane, rightSection);
        GameEndAnnouncement gameEndAnnouncement = new GameEndAnnouncement("Player 1", "Player 2", "win", 
                () -> {
                    System.out.println("return to main menu");
                }
        );
        this.getChildren().addAll(backgroundPane, contentPane, gameEndAnnouncement);
    }

    abstract protected void handleOnMovePiece(String currentTurn);
    
    public void setPlayerTop(String name, String elo, String side){
        playerTop = new PlayerInfo(name, elo, side);
        playerSectionTop.setInfo(name, elo, side);
    }

    public void setPlayerBottom(String name, String elo, String side){
        playerBottom = new PlayerInfo(name, elo, side);
        playerSectionBottom.setInfo(name, elo, side);
    }

    public void hanldeGameEnd(String currentTurn){

    }
}
