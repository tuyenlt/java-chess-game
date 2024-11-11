package network;
import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import network.RequestAndResponse.ClassRegester;
import network.RequestAndResponse.ErrorResponse;
import network.RequestAndResponse.LoginRequest;
import network.RequestAndResponse.RankingListRequest;
import network.RequestAndResponse.RankingListResponse;
import network.RequestAndResponse.RegisterRequest;
import network.RequestAndResponse.SimpleResponse;
import network.RequestAndResponse.UserResponse;
import network.database.DatabaseConnection;


public class MainServer {
    private Server server;
    private int tcpPort;
    private int udpPort;
    

    public MainServer(int tcpPort ,int udpPort){
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        server = new Server();
        ClassRegester.register(server);

    }


    public void run() throws IOException{
        server.start();
        server.bind(tcpPort, udpPort);
        server.addListener(new Listener(){
            public void received(Connection connection, Object object){
                if(object instanceof LoginRequest){
                    handleLogin(connection, object);
                }

                if(object instanceof RegisterRequest){
                    handleRegister(connection, object);
                }

                if(object instanceof RankingListRequest){
                    handleGetRankingList(connection, object);
                }
            }
        });
    }

    private void handleLogin(Connection connection, Object object){
        LoginRequest loginRequest =  (LoginRequest)object;
        try{
            UserResponse userResponse = DatabaseConnection.loginAuthentication(loginRequest);
            connection.sendTCP(userResponse);
        }catch(Exception error){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.error = error.getMessage();
            connection.sendTCP(errorResponse);
        }
            
    }


    private void handleRegister(Connection connection, Object object){
        RegisterRequest registerRequest = (RegisterRequest)object;
        SimpleResponse response = DatabaseConnection.registerNewUser(registerRequest);
        connection.sendTCP(response);
    }


    
    private void handleGetRankingList(Connection connection, Object object){
        RankingListRequest rankingListRequest = (RankingListRequest)object;
        RankingListResponse rankingListResponse = DatabaseConnection.getRankingList(rankingListRequest);
        connection.sendTCP(rankingListResponse);
    }
    
    
}
