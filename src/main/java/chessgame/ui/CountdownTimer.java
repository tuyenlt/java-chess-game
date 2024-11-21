package chessgame.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class CountdownTimer {
    private int timeRemaining;
    private Label timerLabel; // Label to display the countdown
    private Thread timerThread;
    private volatile boolean running = false;

    public CountdownTimer(int startTimeInSeconds) {
        this.timeRemaining = startTimeInSeconds;
    }

    public void setLabel(Label timerLabel) {
        this.timerLabel = timerLabel;
        Platform.runLater(() -> timerLabel.setText(formatTime(timeRemaining)));
    }

    public void start() {
        if (running || timerLabel == null) return;
        running = true;

        timerThread = new Thread(() -> {
            try {
                while (timeRemaining > 0 && running) {
                    Thread.sleep(1000);
                    timeRemaining--;
                    Platform.runLater(() -> timerLabel.setText(formatTime(timeRemaining)));
                }
                if (timeRemaining == 0) {
                    Platform.runLater(() -> timerLabel.setText("Time's up!"));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void stop() {
        running = false;
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}