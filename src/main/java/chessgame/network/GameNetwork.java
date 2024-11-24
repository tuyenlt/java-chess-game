package chessgame.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import chessgame.network.packets.GeneralPackets.FindGameRequest;
import chessgame.network.packets.GeneralPackets.FindGameResponse;
import chessgame.network.packets.GeneralPackets.HistoryGameRequest;
import chessgame.network.packets.GeneralPackets.HistoryGameResponse;
import chessgame.network.packets.GeneralPackets.LoginRequest;
import chessgame.network.packets.GeneralPackets.LoginResponse;
import chessgame.network.packets.GeneralPackets.MsgPacket;
import chessgame.network.packets.GeneralPackets.ProfileViewRequest;
import chessgame.network.packets.GeneralPackets.ProfileViewResponse;
import chessgame.network.packets.GeneralPackets.RankingListRequest;
import chessgame.network.packets.GeneralPackets.RankingListResponse;
import chessgame.network.packets.GeneralPackets.RegisterRequest;
import chessgame.network.packets.GeneralPackets.RegisterResponse;
import chessgame.network.packets.IngamePackets.GameEndResponse;
import chessgame.network.packets.IngamePackets.GameStateResponse;
import chessgame.network.packets.IngamePackets.MovePacket;
import chessgame.network.packets.PacketsRegester;

public class GameNetwork {
    private Client client;
    private int timeout;
    private int tcpPort;
    private int udpPort;
    private String serverAddr;
    private boolean isConnected = false;
    private LoginResponse user = null; 
    private IngameResponseHandler responseHandle;

    public GameNetwork(int timeout, int tcpPort, int udpPort, String serverAddr) {
        this.timeout = timeout;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverAddr = serverAddr;
        client = new Client();
        PacketsRegester.register(client);
    }

    public void setResponHandler(IngameResponseHandler ingameResponseHandler){
        this.responseHandle = ingameResponseHandler;
    }

    public void connectGameServer() throws IOException {
        isConnected = false;
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                isConnected = true;  
                client.sendTCP(user);
            }

            public void received(Connection connection, Object object) { 

                if (object instanceof MsgPacket) {
                    responseHandle.handleMsgPacket((MsgPacket) object);
                }

                if (object instanceof MovePacket){
                    responseHandle.handleMovePacket((MovePacket)object);
                }

                if (object instanceof GameStateResponse){
                    responseHandle.handleGamestateUpdate((GameStateResponse)object);
                }

                if (object instanceof GameEndResponse){
                    responseHandle.handleGameEnd((GameEndResponse)object);
                }
            }

        });


        new Thread("Connect") {
            public void run() {
                try {
                    client.connect(timeout, serverAddr, tcpPort, udpPort);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();

        while (!isConnected) {
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendRequest(Object object){
        if(object instanceof MsgPacket){
            client.sendTCP((MsgPacket)object);
        }

        if(object instanceof MovePacket){
            client.sendTCP((MovePacket)object);
        }
    }
}
