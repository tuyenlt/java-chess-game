import java.io.IOException;
import java.util.Scanner;
import network.ClientNetwork;
import network.ClientResponseHandle;
import network.IngameResponseHandler;
import network.packets.GeneralPackets.*;
import network.packets.IngamePackets.*;
import network.packets.IngamePackets.GameEndResponse;
import network.packets.IngamePackets.GameStateResponse;
import network.packets.IngamePackets.MovePacket;


class GameClient implements ClientResponseHandle{

    @Override
    public void handleLoginFail(ErrorResponse error) {
        // TODO Auto-generated method stub
        System.out.println("Login fail");
    }

    @Override
    public void handleLoginSuccess(LoginResponse user) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleProfileView(ProfileViewResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleRankingList(RankingListResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleRegister(MsgPacket response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleRegisterResponse(RegisterResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleHistoryGame(HistoryGameResponse response) {
        // TODO Auto-generated method stub
        
    }
    
}

class inGameClient implements IngameResponseHandler{

    @Override
    public void handleGameEnd(GameEndResponse gameEndResponse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleGamestateUpdate(GameStateResponse gameStateResponse) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleMovePacket(MovePacket movePacket) {
        // TODO Auto-generated method stub
        
    }
    
}

public class ClientTest{
    public static void main(String[] args) throws IOException, InterruptedException {
        ClientNetwork client = new ClientNetwork(5000, 5555, 6666, "192.168.1.32");
        // client.run();
        GameClient gameClient = new GameClient();
        inGameClient inGameClient = new inGameClient();
        client.setUiResponseHandler(gameClient); 
        client.setIngameResponHandler(inGameClient);
        client.connectMainServer();  
        client.sendRequest(new FindGameRequest("tuyenlt", 100));
        Thread.sleep(3000);  
        client.SendIngameRequest(new MovePacket(0, 0, 2, 2));       
        Thread.sleep(1000);         
        Thread.sleep(5000);         
    }


}
