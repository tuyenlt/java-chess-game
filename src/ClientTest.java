import java.io.IOException;
import java.util.Scanner;
import network.ClientNetwork;
import network.ClientResponseHandle;
import network.RequestAndResponse.GeneralConnectionManager.ErrorResponse;
import network.RequestAndResponse.GeneralConnectionManager.LoginResponse;
import network.RequestAndResponse.GeneralConnectionManager.MsgPacket;
import network.RequestAndResponse.GeneralConnectionManager.ProfileViewResponse;
import network.RequestAndResponse.GeneralConnectionManager.RankingListResponse;
import network.RequestAndResponse.GeneralConnectionManager.RegisterResponse;
import network.RequestAndResponse.GeneralConnectionManager.ReplayGameResponse;


class GameClient implements ClientResponseHandle{

    @Override
    public void handleLoginFail(ErrorResponse error) {
        // TODO Auto-generated method stub
        
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
    public void handleReplayGame(ReplayGameResponse response) {
        // TODO Auto-generated method stub
        
    }
    
}

public class ClientTest{
    public static void main(String[] args) throws IOException {
        ClientNetwork client = new ClientNetwork(5000, 5555, 5556, "192.168.1.32");
        // client.run();
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            int stX = sc.nextInt();
            int stY = sc.nextInt();
            int enX = sc.nextInt();
            int enY = sc.nextInt();
            client.sendMove(stX, stY, enX, enY);
        }
        
    }


}
