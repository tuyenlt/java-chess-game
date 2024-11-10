package network;

import java.io.IOException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import network.RequestAndResponse.MoveRequest;
import network.RequestAndResponse.SimpleRequest;
import network.RequestAndResponse.SimpleResponse;

public class ClientNetwork {
    private Client client;
    private int timeout;
    private int TCPPort;
    private int UDPPort;
    private String serverAddr;
    public boolean isConnected = false;

    public ClientNetwork(int timeout, int tCPPort, int uDPPort, String serverAddr) {
        this.timeout = timeout;
        TCPPort = tCPPort;
        UDPPort = uDPPort;
        this.serverAddr = serverAddr;
        client = new Client();
        Kryo kryo = client.getKryo();
        kryo.register(SimpleRequest.class);
        kryo.register(SimpleResponse.class);
        kryo.register(MoveRequest.class);
    }

    public void run() throws IOException {
        client.start();
        client.addListener(new Listener() {

            public void connected(Connection connection) {
                SimpleRequest req = new SimpleRequest();
                req.msg = "my name is luis";
                connection.sendTCP(req);
                isConnected = true;  
            }

            public void received(Connection connection, Object object) {
                if (object instanceof SimpleResponse) {
                    SimpleResponse response = (SimpleResponse) object;
                    System.out.println(response.msg);
                }
            }

        });


        new Thread("Connect") {
            public void run() {
                try {
                    client.connect(timeout, serverAddr, TCPPort, UDPPort);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
    }

    public void sendMsg(String msg) {
        if (isConnected) {
            SimpleRequest req = new SimpleRequest();
            req.msg = msg;
            client.sendTCP(req); 
        } else {
            System.out.println("Not connected yet. Please try again later.");
        }
    }

    public void sendMove(int stX, int stY, int enX, int enY){
        if(isConnected){
            MoveRequest req = new MoveRequest(stX,stY,enX,enY);
            client.sendTCP(req);
        }
    }
}
