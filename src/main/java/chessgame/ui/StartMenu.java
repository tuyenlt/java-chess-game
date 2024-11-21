package chessgame.ui;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Start Menu");
        title.setFont(Font.font("Arial", 36));

        // Buttons
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button offlineButton = new Button("Offline Mode");
        Button quitButton = new Button("Quit");

        // Set Button Styles
        loginButton.setPrefWidth(200);
        registerButton.setPrefWidth(200);
        offlineButton.setPrefWidth(200);
        quitButton.setPrefWidth(200);

        // Button Actions
        loginButton.setOnAction(e -> System.out.println("Login clicked!"));
        registerButton.setOnAction(e -> System.out.println("Register clicked!"));
        offlineButton.setOnAction(e -> System.out.println("Offline Mode clicked!"));
        quitButton.setOnAction(e -> {
            System.out.println("Quitting the application...");
            primaryStage.close();
        });

        // Layout
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title, loginButton, registerButton, offlineButton, quitButton);

        // Scene
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Start Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
