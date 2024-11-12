package network.RequestAndResponse;

import network.RequestAndResponse.GeneralConnectionManager.*;
import network.RequestAndResponse.IngameConnectionManager.*;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class PacketsRegester {
    static public void register(EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        kryo.register(MsgPacket.class);
        kryo.register(LoginRequest.class);
        kryo.register(LoginResponse.class);
        kryo.register(RegisterRequest.class);
        kryo.register(ProfileViewRequest.class);
        kryo.register(ProfileViewResponse.class);
        kryo.register(ReplayGameResponse.class);
        kryo.register(ReplayGameRequest.class);
        kryo.register(RankingListRequest.class);
        kryo.register(RankingListResponse.class);
        kryo.register(FindGameRequest.class);
        kryo.register(FindGameResponse.class);
        kryo.register(MovePacket.class);
        kryo.register(GameStateResponse.class);
    }
}