package chessgame.game;

import chessgame.ui.GameOptionsMenu;
import chessgame.ui.ReplayBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HistoryGameReplay extends StackPane {
    private VBox rightSection = new VBox();
    private GameOptionsMenu gameOptionsMenu;
    private GameOptionsMenu replayMenu;
    private AnchorPane contentPane;
    private ReplayBoard replayBoard;
    private boolean isAutoPlay = false; 
    private Timeline autoPlayTimeline;
    private Runnable onReturn = () -> {
        System.out.println("return");
    };

    public HistoryGameReplay(String moves, boolean isBoardReverse) {
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

        replayBoard = new ReplayBoard(moves, isBoardReverse);

        rightSection.setLayoutX(720);
        rightSection.setLayoutY(0);
        rightSection.setPrefSize(560, 720);

        gameOptionsMenu = new GameOptionsMenu();
        gameOptionsMenu.addButton("Quit Game", "quit-button", event -> onReturn.run());

        replayMenu = new GameOptionsMenu();

        replayMenu.addButton("Prev", "custom-button", event -> {
            replayBoard.prev();
        });
        
        replayMenu.addButton("Start/ Stop", "custom-button", event -> {
            toggleAutoPlay();  // Toggle auto-play on button press
        });
        
        replayMenu.addButton("Next", "custom-button", event -> {
            replayBoard.next();
        });

        rightSection.getChildren().addAll(gameOptionsMenu, replayMenu);
        contentPane.getChildren().addAll(rightSection, replayBoard);
        this.getChildren().addAll(backgroundPane, contentPane);
    }

    private void toggleAutoPlay() {
        if (isAutoPlay) {
            autoPlayTimeline.stop();
        } else {
            autoPlayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                Platform.runLater(()->{
                    replayBoard.next();
                });
            }));
            autoPlayTimeline.setCycleCount(Timeline.INDEFINITE);
            autoPlayTimeline.play();
        }
        isAutoPlay = !isAutoPlay; 
    }

    public void setOnReturn(Runnable onReturn){
        this.onReturn = onReturn;
    }
}
