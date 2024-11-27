package chessgame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class AnimationUtils {
    public static void applyEffect(AnchorPane anchorPane, double duration) {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), anchorPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(duration), anchorPane);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.play();
    }
}


