package network.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import network.packets.GeneralPackets.*;
import network.packets.IngamePackets.*;

public class PacketsRegester {
    static public void register(EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        kryo.register(MsgPacket.class);
        kryo.register(LoginRequest.class);
        kryo.register(LoginResponse.class);
        kryo.register(RegisterRequest.class);
        kryo.register(ProfileViewRequest.class);
        kryo.register(ProfileViewResponse.class);
        kryo.register(HistoryGameResponse.class);
        kryo.register(HistoryGameRequest.class);
        kryo.register(RankingListRequest.class);
        kryo.register(RankingListResponse.class);
        kryo.register(FindGameRequest.class);
        kryo.register(FindGameResponse.class);
        kryo.register(MovePacket.class);
        kryo.register(GameStateResponse.class);
        kryo.register(ErrorResponse.class);
        kryo.register(ConnectedPacket.class);
    }
}