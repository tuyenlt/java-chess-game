package network;

import network.RequestAndResponse.*;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryo.Kryo;


import java.io.IOException;


public class ServerNetwork{
    private Server server;
    private int TCPPort;
    private int UDPPort;
    private String[][] broad;

    public ServerNetwork(int TCPPort, int UDPPort){
        this.TCPPort = TCPPort;
        this.UDPPort = UDPPort;
        server = new Server();
        Kryo kryo = server.getKryo();
        kryo.register(SimpleRequest.class);
        kryo.register(SimpleResponse.class);
        kryo.register(MoveRequest.class);
        broadInit();
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

    public void run() throws IOException{
        server.start();
        server.bind(TCPPort, UDPPort);
        System.out.println("start listening for you guy");
        server.addListener(new Listener(){
            public void received (Connection connection, Object object) {
                if (object instanceof SimpleRequest) {
                   handleSimpleRequest(connection, object);
                }

                if (object instanceof MoveRequest){
                    handleMoveRequest(connection, object);
                }
            }
        });
    }
    private void handleSimpleRequest(Connection connection, Object object){
        SimpleRequest request = (SimpleRequest)object;
        System.out.println(request.msg);

        SimpleResponse response = new SimpleResponse();
        response.msg = "Welcome to our house bitch";
        connection.sendTCP(response);
    }

    private void handleMoveRequest(Connection connection, Object object){
        MoveRequest request = (MoveRequest)object;
        System.out.println(request);
        broad[request.enX][request.enY] = broad[request.stX][request.stY];
        broad[request.stX][request.stY] = "";
        SimpleResponse response = new SimpleResponse();
        
        response.msg = getState();
        connection.sendTCP(response);
    }
}
