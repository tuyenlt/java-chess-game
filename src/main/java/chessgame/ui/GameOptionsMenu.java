package chessgame.ui;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class GameOptionsMenu extends HBox {

    public GameOptionsMenu() {
        // Set the size of the HBox
        this.setPrefSize(560, 60);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }

    public void addButton(String name,String styleClass ,Consumer onAction){
        Button newButton = new Button(name);
        newButton.getStyleClass().add(styleClass);
        newButton.setOnAction(event -> onAction.accept(event));
        this.getChildren().add(newButton);
    }

    public void addNode(Node node){
        this.getChildren().add(node);
    }
}
