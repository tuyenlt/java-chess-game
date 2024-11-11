package network;

import java.io.IOException;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import network.RequestAndResponse.ClassRegester;
import network.RequestAndResponse.FindGame;
import network.RequestAndResponse.MoveRequest;
import network.RequestAndResponse.SimpleRequest;
import network.RequestAndResponse.SimpleResponse;
import network.RequestAndResponse.UserResponse;

public class ClientNetwork {
    private Client client;
    private int timeout;
    private int tcpPort;
    private int udpPort;
    private String serverAddr;
    public boolean isConnected = false;
    private ClientResponseHandle onResponse;

    public ClientNetwork(int timeout, int tcpPort, int udpPort, String serverAddr) {
        this.timeout = timeout;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverAddr = serverAddr;
        client = new Client();
        ClassRegester.register(client);
    }

    public void connectMainServer() throws IOException {
        isConnected = false;
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                isConnected = true;  
            }

            public void received(Connection connection, Object object) {
                if (object instanceof SimpleResponse) {
                    SimpleResponse response = (SimpleResponse) object;
                    System.out.println(response.msg);
                }

                if (object instanceof FindGame.Response){
                    handleNewGame(connection, object);
                }

                if (object instanceof UserResponse){
                    onResponse.handleLoginSuccess((UserResponse)object);
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


    public void connectGameServer(int serverTcpPort, int serverUdpPort) throws IOException {
        isConnected = false;
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                isConnected = true;  
            }

            public void received(Connection connection, Object object) {
                if (object instanceof SimpleResponse) {
                    SimpleResponse response = (SimpleResponse) object;
                    System.out.println(response.msg);
                }

                if (object instanceof FindGame.Response){
                    handleNewGame(connection, object);
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

    private void handleNewGame(Connection connection, Object object){
        try{
            FindGame.Response newServerInfo = (FindGame.Response)object;
            client.close();
            connectGameServer(newServerInfo.tcpPort, newServerInfo.udpPort);
        }catch(IOException ex){

        }
    }

    private void handleGameEnd(Connection connection, Object object){
        
    }

    public void sendMsg(String msg) {
        SimpleRequest req = new SimpleRequest();
        req.msg = msg;
        client.sendTCP(req); 
    }

    public void findGameRequest(String userId, int elo){
        FindGame.Request request = new FindGame.Request();
        request.userId = userId;
        request.elo = elo;
        client.sendTCP(request);
    }

    public void sendMove(int stX, int stY, int enX, int enY){
        MoveRequest req = new MoveRequest(stX,stY,enX,enY);
        client.sendTCP(req);
    }
}
