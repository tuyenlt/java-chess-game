package chessgame.game;

import chessgame.logic.Move;
import chessgame.network.GameNetwork;
import chessgame.network.IngameResponseHandler;
import chessgame.network.packets.GeneralPackets.MsgPacket;
import chessgame.network.packets.IngamePackets.GameEndResponse;
import chessgame.network.packets.IngamePackets.GameStateResponse;
import chessgame.network.packets.IngamePackets.MovePacket;
import chessgame.network.packets.IngamePackets.OpponentInfo;
import chessgame.ui.GameEndAnnouncement;
import javafx.application.Platform;

public class TwoPlayerOnlineMode extends MainGame implements IngameResponseHandler{

    private GameNetwork client;
    
    public TwoPlayerOnlineMode(boolean isBoardReverse){
        super("singlePlayer", isBoardReverse);
        gameOptionsMenu.addButton("Resign", "custom-button", (event)->{
            client.sendRequest(new MsgPacket("/surrender"));
        });
    }

    public void setClient(GameNetwork client){
        this.client = client;
    }

    @Override
    protected void handleOnMovePiece(String currentTurn) {
        if(currentTurn.equals(playerTop.side)){
            if(boardPane.getLastMove() == null){
                return;
            }
            String newMove = boardPane.getLastMove().toString();
            System.out.println(newMove);
            client.sendRequest(new MovePacket(newMove));
        }        
    }


    @Override
    public void checkGameEnd(String currentTurn){
        return;
    }

    @Override
    public void handleGameEnd(GameEndResponse gameEndResponse) {
        String winnerName = "None";
        if(gameEndResponse.state == 0){
            winnerName = playerTop.name;
        }else if(gameEndResponse.state == 1){
            winnerName = playerBottom.name;
        }
        GameEndAnnouncement gameEndAnnouncement = new GameEndAnnouncement(
            playerTop.name, 
            playerBottom.name,
            gameEndResponse.state == 0.5 ? "Draw" : winnerName + " win", 
            String.valueOf(gameEndResponse.eloChange), 
            () -> {
                onGameEnd.run();
            }
        );
        Platform.runLater(()->{
            getChildren().add(gameEndAnnouncement);
        });
    }

    @Override
    public void handleGamestateUpdate(GameStateResponse gameStateResponse) {
        
    }

    @Override
    public void handleMovePacket(MovePacket movePacket) {
        if(boardPane.getCurrentTurn().equals(playerTop.side)){
            Platform.runLater(()->{
                boardPane.movePiece(new Move(movePacket.move));
           });
        }
    }

    @Override
    public void onReciveOpponentInfo(OpponentInfo opponentInfo) {
        Platform.runLater(() -> {
            setPlayerTop(opponentInfo.name, String.valueOf(opponentInfo.elo), opponentInfo.side, true);        
        });
    }

    @Override
    public void handleMsgPacket(MsgPacket response) {
        System.out.println(response.msg);        
    }
}
