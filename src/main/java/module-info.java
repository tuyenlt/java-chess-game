module chessgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kryo;
    requires kryonet;

    opens chessgame.ui to javafx.fxml;
    exports chessgame;
    exports chessgame.network.packets;
}
