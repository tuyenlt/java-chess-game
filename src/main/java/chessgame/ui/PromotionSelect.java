package chessgame.ui;

import java.util.function.Consumer;

import chessgame.logic.Move;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PromotionSelect extends VBox{
    private String color;
    private boolean isTopDown = true;
    private int size;
    private Consumer<String> onFinish;
    private Move promotionMove;

    public PromotionSelect(String color, boolean isTopDown, int size) {
        super(0);
        this.color = color;
        this.isTopDown = isTopDown;
        this.size = size;
        setViewOrder(-1);
        setVisible(false);
        setLayoutX(0);
        setBackground(new Background(
            new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)
        ));
        setLayoutX(0);
        if(isTopDown){
            getChildren().add(createPieceItem(color + "Q"));
            getChildren().add(createPieceItem(color + "R"));
            getChildren().add(createPieceItem(color + "B"));
            getChildren().add(createPieceItem(color + "N"));
        }else{
            getChildren().add(createPieceItem(color + "N"));
            getChildren().add(createPieceItem(color + "B"));
            getChildren().add(createPieceItem(color + "R"));
            getChildren().add(createPieceItem(color + "Q"));
        }
    }

    private ImageView createPieceItem(String name){
        Image image = new Image(getClass().getResource("/chessgame/image/pieces/" + name + ".png").toExternalForm());
        ImageView pieceImage = new ImageView(image);
        pieceImage.setFitWidth(size);
        pieceImage.setFitHeight(size);
        pieceImage.setPickOnBounds(true);
        pieceImage.setOnMouseClicked(event ->{
            onFinish.accept(name);     
            setVisible(false);       
        });

        pieceImage.setStyle("-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);");

        pieceImage.setOnMouseClicked(event -> {
            onFinish.accept(name.substring(1).toLowerCase());
            setVisible(false);
        });

        // Mouse enter for highlight
        pieceImage.setOnMouseEntered(event -> {
            pieceImage.setStyle("-fx-effect: dropshadow(gaussian, yellow, 20, 0.5, 0, 0);");
            pieceImage.setScaleX(1.1);
            pieceImage.setScaleY(1.1);
        });

        // Mouse exit to remove highlight
        pieceImage.setOnMouseExited(event -> {
            pieceImage.setStyle("-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);");
            pieceImage.setScaleX(1.0); 
            pieceImage.setScaleY(1.0);
        });
        return pieceImage;
    }

    public void setOnFinish(Consumer<String> consumer){
        onFinish = consumer;        
    }

    public String getColor(){
        return color;
    }

    public void setPromotionMove(Move promotionMove){
        this.promotionMove = promotionMove;
    }

    public Move getPromotionMove(){
        return promotionMove;
    }
}
