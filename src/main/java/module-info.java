module chessgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens chessgame.ui to javafx.fxml;
    exports chessgame;
}
