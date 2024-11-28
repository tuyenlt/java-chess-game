package chessgame.ui;

import chessgame.utils.ResourcesHanlder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class PlayerSection extends Pane {
    private String name;
    private String elo;
    private CountdownTimer timer;
    private Label timerLabel;
    private Label nameLabel;
    private Label eloLabel;
    private ImageView indicator;
    private String side;
    private int maxTime = 600;

    public PlayerSection(String name, String elo, int initialTimeInSeconds, String side) {
        this.name = name;
        this.elo = elo;
        this.side = side;
        this.maxTime = initialTimeInSeconds;
        this.setPrefSize(560, 330);
        this.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        indicator = ResourcesHanlder.createAvatarView(name, false);
        indicator.setLayoutX(20);
        indicator.setLayoutY(20);

        nameLabel = new Label(name);
        eloLabel = new Label(elo);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        eloLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        nameLabel.setLayoutX(100);
        nameLabel.setLayoutY(30);
        eloLabel.setLayoutX(100);
        eloLabel.setLayoutY(60);

        timerLabel = new Label("10:00");
        timerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        timerLabel.setLayoutX(460);
        timerLabel.setLayoutY(20);

        timer = new CountdownTimer(initialTimeInSeconds);
        timer.setLabel(timerLabel);

        this.getChildren().addAll(indicator, nameLabel, eloLabel, timerLabel);
    }

    public void startTimer() {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        timer.start();
    }

    public void stopTimer() {
        this.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        timer.stop();
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public void resetTime() {
        timerLabel.setText("10:00");
        timer = new CountdownTimer(maxTime);
        timer.setLabel(timerLabel);
    }

    private Image getImage(String name) {
        String imagePath = getClass().getResource("/chessgame/avatar/" + name + ".jpg").toExternalForm();
        System.out.println(imagePath);
        return ResourcesHanlder.cropImageToSquare(new Image(imagePath, 60, 60, true, true));
    }

    public void setUserAvatar(String name) {
        indicator.setImage(ResourcesHanlder.getAvatarImage(name));
    }

    public void setInfo(String name, String elo, String side, boolean isOnline) {
        this.name = name;
        this.elo = elo;
        this.side = side;
        Image avatarImage;
        if (isOnline) {
            avatarImage = ResourcesHanlder.getAvatarImage(name);
        }else{
            avatarImage = getImage(name);
        }
        indicator.setImage(avatarImage);
        nameLabel.setText(name);
        eloLabel.setText(elo);
    }
}
