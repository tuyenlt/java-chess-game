package chessgame.game;

import chessgame.ui.BoardPane;
import chessgame.ui.GameEndAnnouncement;
import chessgame.ui.GameOptionsMenu;
import chessgame.ui.PlayerSection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

abstract public class MainGame extends StackPane {
    protected int gameTime = 600;
    protected BoardPane boardPane;
    protected VBox rightSection = new VBox();
    protected boolean isBoardReverse = false;
    protected PlayerSection playerSectionTop;
    protected PlayerSection playerSectionBottom;
    protected PlayerInfo playerTop = new PlayerInfo();
    protected PlayerInfo playerBottom = new PlayerInfo();
    protected Stage mainStage;
    protected GameOptionsMenu gameOptionsMenu;
    protected AnchorPane contentPane;
    protected Runnable onGameEnd = () -> {
        System.out.println("game end motherfucker");
    };

    protected class PlayerInfo{
        public String name;
        public String elo;
        public String side;
        public PlayerInfo(){
            name = "Player (Not You Bitch)";
            elo = "???";
            side = "w";
        }
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

        contentPane = new AnchorPane();
        contentPane.setPrefSize(1280, 720);

        rightSection.setLayoutX(720);
        rightSection.setLayoutY(0);
        rightSection.setPrefSize(560, 720);
        rightSection.setStyle("-fx-background-image: url('/chessgame/image/bg.png'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-repeat: no-repeat; " +
                                "-fx-background-position: center;");

        playerSectionTop = new PlayerSection("Player1", "???", gameTime, "b");
        playerSectionBottom = new PlayerSection("Player2", "???", gameTime, "w");

        gameOptionsMenu = new GameOptionsMenu();
        gameOptionsMenu.addButton("Quit Game", "quit-button", event -> onGameEnd.run());

        rightSection.getChildren().addAll(gameOptionsMenu,playerSectionTop, playerSectionBottom);

        createBoard(mode, isBoardReverse);
        
        contentPane.getChildren().add(rightSection);
        this.getChildren().addAll(backgroundPane, contentPane);
    }

    abstract protected void handleOnMovePiece(String currentTurn);

    public void createBoard(String mode, boolean isBoardReverse){
        String topSide = "b";
        String botSide = "w";
        if(isBoardReverse){
            topSide = "w";
            botSide = "b";
        }
        playerSectionTop.setSide(topSide);
        playerSectionTop.stopTimer();
        playerSectionTop.resetTime();
        
        playerSectionBottom.setSide(botSide);
        playerSectionBottom.stopTimer();
        playerSectionBottom.resetTime();

        if(boardPane != null){
            contentPane.getChildren().remove(boardPane);
        }
        boardPane = new BoardPane(mode, isBoardReverse);
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
                checkGameEnd(currentTurn);
            }).start();
        });
        boardPane.start();
        if(boardPane.getCurrentTurn().equals(topSide)){
            playerSectionTop.startTimer();
        }else{
            playerSectionBottom.startTimer();
        }
        contentPane.getChildren().add(boardPane);
    }
    
    public void setPlayerTop(String name, String elo, String side,boolean isOnline){
        playerTop = new PlayerInfo(name, elo, side);
        playerSectionTop.setInfo(name, elo, side , isOnline);
        if(isOnline){
            playerSectionTop.setUserAvatar(name);
        }
    }

    public void setPlayerBottom(String name, String elo, String side, boolean isOnline){
        playerBottom = new PlayerInfo(name, elo, side);
        playerSectionBottom.setInfo(name, elo, side, isOnline);
        if (isOnline) {
            playerSectionBottom.setUserAvatar(name);
        }
    }

    public void checkGameEnd(String currentTurn){
        String gameState = boardPane.getGameState();
        if(!gameState.equals("ongoing")){
            String winnerName = "None";
            if(gameState.equals("win")){
                if(currentTurn.equals(playerTop.side)){
                    winnerName = playerBottom.name;
                }else{
                    winnerName = playerTop.name;
                }
            }
            GameEndAnnouncement gameEndAnnouncement = new GameEndAnnouncement(playerTop.name, playerBottom.name,winnerName + " win", "None", 
            () -> {
                onGameEnd.run();
            });
            Platform.runLater(()->{
                getChildren().add(gameEndAnnouncement);
            });
        }
    }

    public void setOnGameEnd(Runnable onGameEnd){
        this.onGameEnd = onGameEnd;
    }
}
