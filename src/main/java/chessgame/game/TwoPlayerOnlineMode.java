package chessgame.game;

import chessgame.network.IngameResponseHandler;
import chessgame.network.packets.GeneralPackets.MsgPacket;
import chessgame.network.packets.IngamePackets.GameEndResponse;
import chessgame.network.packets.IngamePackets.GameStateResponse;
import chessgame.network.packets.IngamePackets.MovePacket;
import chessgame.network.packets.IngamePackets.OpponentInfo;
import chessgame.ui.BoardPane;
import chessgame.ui.CountdownTimer;
import chessgame.ui.PlayerSection;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class TwoPlayerOnlineMode implements IngameResponseHandler{

    private CountdownTimer countdownTimerTop;
    private CountdownTimer countdownTimerBottom;

    @FXML
    private VBox rightSection;

    @FXML
    private BoardPane boardPane;
    
    @FXML
    public void initialize() {
        boardPane.setGameMode("twoPlayer");
        boardPane.setReverse(false);
        PlayerSection playerSectionTop = new PlayerSection("Player 1", "200", 600, "b");
        PlayerSection playerSectionBottom = new PlayerSection("Player 1", "200", 600, "w");
        rightSection.getChildren().addAll(playerSectionTop);
        rightSection.getChildren().addAll(playerSectionBottom);
        boardPane.setOnMovePiece((tmp) -> {
            new Thread(() -> {
                if(boardPane.getCurrentTurn().equals("w")) {
                    playerSectionTop.stopTimer();
                    playerSectionBottom.startTimer();
                }else{
                    playerSectionTop.startTimer();
                    playerSectionBottom.stopTimer();
                }
            }).start();
        });
        boardPane.start();
        playerSectionBottom.startTimer();
    }


    @Override
    public void handleGameEnd(GameEndResponse gameEndResponse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleGamestateUpdate(GameStateResponse gameStateResponse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleMovePacket(MovePacket movePacket) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onReciveOpponentInfo(OpponentInfo opponentInfo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleMsgPacket(MsgPacket response) {
        // TODO Auto-generated method stub
        
    }
}
