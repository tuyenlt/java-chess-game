package ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginForm {
    private Stage primaryStage;

    public LoginForm(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        // Title
        Text title = new Text("Login Form");
        title.setFont(Font.font("Arial", 24));

        // Username Label and Field
        javafx.scene.control.Label usernameLabel = new javafx.scene.control.Label("Username:");
        javafx.scene.control.TextField usernameField = new javafx.scene.control.TextField();
        usernameField.setPromptText("Enter your username");

        // Password Label and Field
        javafx.scene.control.Label passwordLabel = new javafx.scene.control.Label("Password:");
        javafx.scene.control.PasswordField passwordField = new javafx.scene.control.PasswordField();
        passwordField.setPromptText("Enter your password");

        // Login Button
        javafx.scene.control.Button loginButton = new javafx.scene.control.Button("Login");
        loginButton.setPrefWidth(100);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Simple validation (you can replace with actual authentication logic)
            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Please fill in all fields.");
            } else {
                System.out.println("Logging in with username: " + username);
            }
        });

        // GridPane Layout
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);

        // Root Layout
        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(title, grid);

        // Scene for LoginForm
        Scene loginScene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}