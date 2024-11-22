package chessgame.game;

import chessgame.network.IngameResponseHandler;
import chessgame.network.packets.IngamePackets.GameEndResponse;
import chessgame.network.packets.IngamePackets.GameStateResponse;
import chessgame.network.packets.IngamePackets.MovePacket;
import chessgame.network.packets.IngamePackets.OpponentInfo;

public class TwoPlayerOnlineMode implements IngameResponseHandler{

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


    
}
