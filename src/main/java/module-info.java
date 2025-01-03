module chessgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kryo;
    requires kryonet;
    requires javafx.graphics;
    requires javafx.media;
    requires java.desktop;

    opens chessgame.ui to javafx.fxml;
    opens chessgame.game to javafx.fxml;
    exports chessgame.game;
    exports chessgame;
    exports chessgame.network;
    exports chessgame.network.packets;
}
