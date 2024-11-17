package network;

import java.io.IOException;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import network.packets.PacketsRegester;
import network.packets.GeneralPackets.*;
import network.packets.IngamePackets.*;

public class ClientNetwork {
    private Client client;
    private int timeout;
    private int tcpPort;
    private int udpPort;
    private String serverAddr;
    private boolean isConnected = false;
    private LoginResponse user = null; 
    private ClientResponseHandle responseHandle;
    private IngameResponseHandler ingameResponseHandler;

    public ClientNetwork(int timeout, int tcpPort, int udpPort, String serverAddr) {
        this.timeout = timeout;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverAddr = serverAddr;
        client = new Client();
        PacketsRegester.register(client);
    }

    public void setUiResponseHandler(ClientResponseHandle clientResponseHandle){
        responseHandle = clientResponseHandle;
    }

    public void setIngameResponHandler(IngameResponseHandler ingameResponseHandler){
        this.ingameResponseHandler = ingameResponseHandler;
    }
    
    //* 
    //* 
    //* main server  ..........................................................................
    public void connectMainServer() throws IOException {
        isConnected = false;
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                isConnected = true;  
            }

            public void received(Connection connection, Object object) {

                if (object instanceof MsgPacket) {
                    MsgPacket response = (MsgPacket) object;
                    System.out.println(response.msg);
                }

                if (object instanceof FindGameResponse){
                    handleNewGame(connection, object);
                }

                if (object instanceof LoginResponse){
                    user = (LoginResponse)object;
                    responseHandle.handleLoginSuccess((LoginResponse)object);
                }

                if (object instanceof ErrorResponse){
                    responseHandle.handleLoginFail((ErrorResponse)object);
                }

                if (object instanceof RegisterResponse){
                    responseHandle.handleRegisterResponse((RegisterResponse)object);
                }

                if (object instanceof ProfileViewResponse){
                    responseHandle.handleProfileView((ProfileViewResponse)object);
                }

                if (object instanceof HistoryGameResponse){
                    responseHandle.handleHistoryGame((HistoryGameResponse)object);
                }

                if (object instanceof RankingListResponse){
                    responseHandle.handleRankingList(null);
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
    
    //* 
    //* 
    //* end */




    //* 
    //* 
    //* game server part ..........................................................................
    public void connectGameServer(int serverTcpPort, int serverUdpPort, String side) throws IOException {
        isConnected = false;
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                isConnected = true;  
                client.sendTCP(user);
            }

            public void received(Connection connection, Object object) { 

                if (object instanceof MsgPacket) {
                    MsgPacket response = (MsgPacket) object;
                    System.out.println(response.msg);
                }

                if (object instanceof MovePacket){
                    ingameResponseHandler.handleMovePacket((MovePacket)object);
                }

                if (object instanceof GameStateResponse){
                    ingameResponseHandler.handleGamestateUpdate((GameStateResponse)object);
                }

                if (object instanceof GameEndResponse){
                    ingameResponseHandler.handleGameEnd((GameEndResponse)object);
                }
            }

        });


        new Thread("Connect") {
            public void run() {
                try {
                    client.connect(timeout, serverAddr, serverTcpPort, serverUdpPort);
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

    //* 
    //* 
    //* end */


    private void handleNewGame(Connection connection, Object object){
        try{
            FindGameResponse newServerInfo = (FindGameResponse)object;
            client.close();
            connectGameServer(newServerInfo.tcpPort, newServerInfo.udpPort, newServerInfo.side);
        }catch(IOException ex){

        }
    }

    public void sendMsg(String msg) {
        MsgPacket req = new MsgPacket();
        req.msg = msg;
        client.sendTCP(req); 
    }

    public void sendRequest(Object object){
        if(object instanceof LoginRequest){
            client.sendTCP((LoginRequest)object);
        }

        if(object instanceof RegisterRequest){
            client.sendTCP((RegisterRequest)object);
        }

        if(object instanceof ProfileViewRequest){
            client.sendTCP((ProfileViewRequest)object);
        }

        if(object instanceof RankingListRequest){
            client.sendTCP((RankingListRequest)object);
        }

        if(object instanceof HistoryGameRequest){
            client.sendTCP((HistoryGameRequest)object);
        }

        if(object instanceof MsgPacket){
            client.sendTCP((MsgPacket)object);
        }

        if(object instanceof FindGameRequest){
            client.sendTCP((FindGameRequest)object);
        }
    }
    
    public void SendIngameRequest(Object object){
        if(object instanceof MsgPacket){
            client.sendTCP((MsgPacket)object);
        }

        if(object instanceof MovePacket){
            client.sendTCP((MovePacket)object);
        }
    }

    public void findGameRequest(int userId, int elo){
        FindGameRequest request = new FindGameRequest();
        request.userId = userId;
        request.elo = elo;
        client.sendTCP(request);
    }
}
