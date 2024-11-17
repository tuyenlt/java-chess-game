package network;

import network.packets.IngamePackets.*;

public interface IngameResponseHandler {
    public void handleMovePacket(MovePacket movePacket);
    public void handleGamestateUpdate(GameStateResponse gameStateResponse);
    public void handleGameEnd(GameEndResponse gameEndResponse);
    public void onReciveOpponentInfo(OpponentInfo opponentInfo);
}
