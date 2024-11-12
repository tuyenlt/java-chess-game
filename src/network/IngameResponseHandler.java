package network;

import network.RequestAndResponse.IngameConnectionManager.*;

public interface IngameResponseHandler {
    public void handleMovePacket(MovePacket movePacket);
    public void handleGamestateUpdate(GameStateResponse gameStateResponse);
    public void handleGameEnd(GameEndResponse gameEndResponse);
}
