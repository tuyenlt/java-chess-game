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
    }
}
