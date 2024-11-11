package network;
import java.io.IOException;
import java.util.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import network.RequestAndResponse.ClassRegester;
import network.RequestAndResponse.ErrorResponse;
import network.RequestAndResponse.FindGame;
import network.RequestAndResponse.HistoryGame;
import network.RequestAndResponse.LoginRequest;
import network.RequestAndResponse.ProfileView;
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
    private ArrayList<WaitingPlayer> waitingPlayers;
    
    class WaitingPlayer{
        public Connection connection;
        public int elo;
        public String playerId;

        public WaitingPlayer(Connection connection,String playerId,int elo) {
            this.connection = connection;
            this.playerId = playerId;
            this.elo = elo;
        }
    }

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

                if(object instanceof HistoryGame.Request){
                    handleGetHistoryGame(connection, object);
                }

                if(object instanceof ProfileView.Request){
                    handleViewProfile(connection, object);
                }

                if(object instanceof FindGame.Request){
                    handleWatingPlayer(connection, object);
                }

            }
        });
    }

    private void handleLogin(Connection connection, Object object){
        LoginRequest request =  (LoginRequest)object;
        try{
            UserResponse response = DatabaseConnection.loginAuthentication(request);
            connection.sendTCP(response);
        }catch(Exception error){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.error = error.getMessage();
            connection.sendTCP(errorResponse);
        }    
    }


    private void handleRegister(Connection connection, Object object){
        RegisterRequest request = (RegisterRequest)object;
        SimpleResponse response = DatabaseConnection.registerNewUser(request);
        connection.sendTCP(response);
    }


    
    private void handleGetRankingList(Connection connection, Object object){
        RankingListRequest request = (RankingListRequest)object;
        RankingListResponse response = DatabaseConnection.getRankingList(request);
        connection.sendTCP(response);
    }

    private void handleViewProfile(Connection connection, Object object){
        ProfileView.Request request = (ProfileView.Request)object;
        ProfileView.Response response = DatabaseConnection.getProfile(request);
        connection.sendTCP(response);
    }
    

    private void handleGetHistoryGame(Connection connection, Object object){
        HistoryGame.Request request = (HistoryGame.Request)object;
        HistoryGame.Response response = DatabaseConnection.getHistoryGame(request);
        connection.sendTCP(response);
    }
    
    private void handleWatingPlayer(Connection connection, Object object){
        FindGame.Request request = (FindGame.Request)object;
        boolean isFoundNewGame = false;
        for(WaitingPlayer waitingPlayer: waitingPlayers){
            if(Math.abs(waitingPlayer.elo - request.elo) <= 200){
                //*! create new game server here
                //*!
                //*!
                GameServer gameServer = new GameServer(waitingPlayer.playerId, request.userId);
                int[] newServerPort = gameServer.getServerPorts();
                FindGame.Response response = new FindGame.Response();
                response.tcpPort = newServerPort[0];
                response.udpPort = newServerPort[1];
                gameServer.run();
                waitingPlayer.connection.sendTCP(response);
                connection.sendTCP(response);
                waitingPlayers.remove(waitingPlayer);
                isFoundNewGame = true;
                break;
            }
        }
        if(!isFoundNewGame){
            waitingPlayers.add(new WaitingPlayer(connection, request.userId, request.elo));
        }
    }
}
