package chessgame.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import chessgame.network.packets.GeneralPackets.*;
import chessgame.network.packets.IngamePackets.*;
import chessgame.network.packets.PacketsRegester;

public class GameClient {
    private Client client;
    private int timeout;
    private int tcpPort;
    private int udpPort;
    private String serverAddr;
    private boolean isConnected = false;
    private IngameResponseHandler responseHandle;

    public GameClient(int timeout, int tcpPort, int udpPort, String serverAddr) {
        this.timeout = timeout;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverAddr = serverAddr;
        client = new Client(8912, 8912);
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
            }

            public void received(Connection connection, Object object) {
                if(object instanceof OpponentInfo){
                    responseHandle.onReciveOpponentInfo((OpponentInfo)object);
                } 

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


        new Thread() {
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
        if(object instanceof InitPacket){
            client.sendTCP((InitPacket)object);
        }
    }
}
