package network;

import com.esotericsoftware.kryonet.Server;

import network.packets.*;
import network.packets.GeneralPackets.*;
import network.packets.IngamePackets.*;
import network.database.DatabaseConnection;
import network.database.Player;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.net.ServerSocket;

import java.io.IOException;



public class GameServer{
    private Server server;
    private String[][] broad;
    private Player whitePlayer;
    private Player blackPlayer;
    private int tcpPort;
    private int udpPort;
    
    public GameServer(int whitePlayerId, int blackPlayerId){
        try{
            whitePlayer = DatabaseConnection.getPlayerInfoById(whitePlayerId);
            blackPlayer = DatabaseConnection.getPlayerInfoById(blackPlayerId);
        }catch (Exception e){

        }
        server = new Server();
        PacketsRegester.register(server);
        try{
            int[] ports = getTwoFreePorts();
            tcpPort = ports[0];
            udpPort = ports[1];
            server.bind(tcpPort, udpPort);
        }catch(IOException ex){
            System.err.println(ex);
        }
        broadInit();
    }

    private int[] getTwoFreePorts() throws IOException {
        int[] ports = new int[2]; 
        try (
            ServerSocket serverSocket1 = new ServerSocket(0);
            ServerSocket serverSocket2 = new ServerSocket(0)
            ) { 
            ports[0] = serverSocket1.getLocalPort(); 
            ports[1] = serverSocket2.getLocalPort(); 
        } 
        return ports; 
    }

    public int[] getServerPorts(){
        int[] ports = {tcpPort, udpPort};
        return ports;
    }

    void broadInit(){
        broad = new String[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                broad[i][j] = "";
            }
        }
        for(int i=0;i<8;i++){
            broad[1][i] = "wP";
            broad[6][i] = "bP";
        }
        broad[0][0] = "wR";
        broad[0][7] = "wR";
        broad[0][1] = "wK";
        broad[0][2] = "wB";
        broad[0][3] = "wKi";
        broad[0][4] = "wQ";
        broad[0][5] = "wB";
        broad[0][6] = "wK";
        broad[0][7] = "wR";


        broad[7][0] = "bR";
        broad[7][1] = "bK";
        broad[7][2] = "bB";
        broad[7][3] = "bKi";
        broad[7][4] = "bQ";
        broad[7][5] = "bB";
        broad[7][6] = "bK";
        broad[7][7] = "bR";
    }


    public String getState(){
        StringBuilder state = new StringBuilder();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                state.append(String.format("%-" + 5 + "s", broad[i][j]));
            }
            state.append("\n");
        }
        return state.toString();
    }

    public void run(){
        server.start();
        System.out.println("start listening for you guy");
        server.addListener(new Listener(){  
            
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            public void received (Connection connection, Object object) {
                if (object instanceof MsgPacket) {
                   handleMsgPacket(connection, object);
                }

                if (object instanceof MovePacket){
                    handleMoveRequest(connection, object);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                                    
                super.disconnected(connection);
            }

            
        });
    }

    private void handleMsgPacket(Connection connection, Object object){
        MsgPacket request = (MsgPacket)object;
        System.out.println(request.msg);

        MsgPacket response = new MsgPacket();
        response.msg = "Welcome to our house bitch";
        connection.sendTCP(response);
    }

    private void handleMoveRequest(Connection connection, Object object){
        MovePacket request = (MovePacket)object;
        System.out.println(request);
        broad[request.enX][request.enY] = broad[request.stX][request.stY];
        broad[request.stX][request.stY] = "";
        MsgPacket response = new MsgPacket();       
        response.msg = getState();
        connection.sendTCP(response);
    }

    private void handlePlayerDisconnect(Connection connection){

    }
}
