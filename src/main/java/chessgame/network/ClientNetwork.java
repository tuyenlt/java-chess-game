package chessgame.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import chessgame.network.packets.PacketsRegester;
import chessgame.network.packets.GeneralPackets.*;

public class ClientNetwork {
    private Client client;
    private int timeout;
    private int tcpPort;
    private int udpPort;
    private String serverAddr;
    private boolean isConnected = false;
    private ClientResponseHandle responseHandle;

    public ClientNetwork(int timeout, int tcpPort, int udpPort, String serverAddr) {
        this.timeout = timeout;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverAddr = serverAddr;
        client = new Client(10000, 10000);
        PacketsRegester.register(client);
    }

    public void setUiResponseHandler(ClientResponseHandle clientResponseHandle){
        responseHandle = clientResponseHandle;
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
                
                if (object instanceof LoginResponse){
                    responseHandle.handleLoginResponse((LoginResponse)object);
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
                    responseHandle.handleRankingList((RankingListResponse)object);
                }

                if (object instanceof FindGameResponse){
                    responseHandle.handleNewGameResonse((FindGameResponse)object);
                }
            }

        });
        client.addListener(new ImageChunkHandler());
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

    public void sendImage(File file, String name) {
        final int CHUNK_SIZE = 8000;
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int chunkIndex = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                ImageChunk chunk = new ImageChunk();
                chunk.fileName = name;
                chunk.chunkIndex = chunkIndex++;
                chunk.totalChunks = (int) Math.ceil((double) file.length() / CHUNK_SIZE);
                chunk.imageData = bytesRead == CHUNK_SIZE ? buffer : java.util.Arrays.copyOf(buffer, bytesRead);

                client.sendTCP(chunk);
                System.out.println("Sent chunk " + chunk.chunkIndex + "/" + chunk.totalChunks + " of file " + name);
            }

            System.out.println("Image " + name + " sent to server in chunks.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to read or send file: " + file.getAbsolutePath());
        }
    }


    public void sendRequest(Object object){
        if(object instanceof MsgPacket){
            client.sendTCP((MsgPacket)object);
        }

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
}
