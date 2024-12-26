package chessgame.game;

import chessgame.ui.GameOptionsMenu;
import chessgame.ui.ReplayBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
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

    private ScrollPane movesScrollPane;
    private FlowPane movesContainer;

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
        gameOptionsMenu.addButton("Quit", "quit-button", event -> onReturn.run());

        replayMenu = new GameOptionsMenu();

        replayMenu.addButton("Prev", "custom-button", event -> {
            replayBoard.prev();
            updateMovesDisplay();
            if(autoPlayTimeline!=null){
                autoPlayTimeline.stop();
            }            
        });
        
        replayMenu.addButton("Start/ Stop", "custom-button", event -> {
            toggleAutoPlay();  // Toggle auto-play on button press
        });
        
        replayMenu.addButton("Next", "custom-button", event -> {
            replayBoard.next();
            updateMovesDisplay();
            if(autoPlayTimeline!=null){
                autoPlayTimeline.stop();
            }
        });

        // Create ScrollPane for moves
        movesScrollPane = new ScrollPane();
        movesScrollPane.setPrefSize(560, 500);
        movesScrollPane.setFitToWidth(true);

        // Create VBox to hold move labels
        movesContainer = new FlowPane();
        movesContainer.setMaxWidth(560);
        movesContainer.setVgap(5);
        movesContainer.setHgap(5);
        movesContainer.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        movesScrollPane.setContent(movesContainer);

        // Add components to rightSection
        rightSection.getChildren().addAll(gameOptionsMenu, movesScrollPane,replayMenu);
        contentPane.getChildren().addAll(rightSection, replayBoard);
        this.getChildren().addAll(backgroundPane, contentPane);

        // Initialize moves display
        updateMovesDisplay();
    }

    private void toggleAutoPlay() {
        if (isAutoPlay) {
            autoPlayTimeline.stop();
        } else {
            autoPlayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                Platform.runLater(() -> {
                    replayBoard.next();
                    updateMovesDisplay();
                });
            }));
            autoPlayTimeline.setCycleCount(Timeline.INDEFINITE);
            autoPlayTimeline.play();
        }
        isAutoPlay = !isAutoPlay; 
    }

    public void setOnReturn(Runnable onReturn) {
        this.onReturn = onReturn;
    }

    private void updateMovesDisplay() {
        movesContainer.getChildren().clear();
        String[] moves = replayBoard.getMoves().split(" ");
        if(moves[0].equals("")){
            return;
        }
        for (int i = 0; i < moves.length; i++) {
            Label moveLabel = new Label((i + 1) + ". " + moves[i]);
            if(i % 2 == 0){
                moveLabel.setStyle("-fx-text-fill: black;-fx-font-size: 16px; -fx-padding: 5px; -fx-background-color: #f0f0f0; -fx-border-radius: 15px;");
            }else{
                moveLabel.setStyle("-fx-text-fill: white;-fx-font-size: 16px; -fx-padding: 5px; -fx-background-color: #111111; -fx-border-radius: 15px;");
            }
            moveLabel.setOnMouseClicked(event -> {
                replayBoard.loadState(movesContainer.getChildren().indexOf(moveLabel) + 1);
                updateMovesDisplay();
            });
            movesContainer.getChildren().add(moveLabel);
        }
    }
}