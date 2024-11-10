import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) {
        // Create a label with text
        Label label = new Label("Hello, JavaFX!");

        // Create a scene with the label and set its dimensions
        Scene scene = new Scene(label, 400, 200);

        // Set the scene to the stage (the main window)
        stage.setScene(scene);

        // Set the title of the window
        stage.setTitle("JavaFX Simple Example");

        // Show the window
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
