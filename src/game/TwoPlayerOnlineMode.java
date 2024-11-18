package game;

import network.ClientNetwork;
import network.IngameResponseHandler;
import network.packets.IngamePackets.GameEndResponse;
import network.packets.IngamePackets.GameStateResponse;
import network.packets.IngamePackets.MovePacket;
import network.packets.IngamePackets.OpponentInfo;

public class TwoPlayerOnlineMode implements IngameResponseHandler{
    private ClientNetwork gameClient;

    public TwoPlayerOnlineMode(ClientNetwork gameClient){
        this.gameClient = gameClient;
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
    
}
