module chessgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kryo;
    requires kryonet;
    requires javafx.graphics;

    opens chessgame.ui to javafx.fxml;
    opens chessgame.game to javafx.fxml;
    exports chessgame;
    exports chessgame.network.packets;
}
