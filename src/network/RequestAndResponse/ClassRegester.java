package network.RequestAndResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class ClassRegester {
    static public void register(EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        kryo.register(MoveRequest.class);
        kryo.register(SimpleRequest.class);
        kryo.register(SimpleResponse.class);
        kryo.register(LoginRequest.class);
        kryo.register(RegisterRequest.class);
        kryo.register(UserResponse.class);
        kryo.register(ErrorResponse.class);
        kryo.register(FindGame.Request.class);
        kryo.register(FindGame.Response.class);
        kryo.register(HistoryGame.Request.class);
        kryo.register(HistoryGame.Response.class);
        kryo.register(RankingListRequest.class);
        kryo.register(RankingListResponse.class);
        kryo.register(ProfileView.Request.class);
        kryo.register(ProfileView.Response.class);
        kryo.register(GameStateResponse.class);
    }
}
