import java.io.IOException;
import java.util.Scanner;
import network.ClientNetwork;
import network.ClientResponseHandle;
import network.packets.GeneralPackets.*;


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

public class ClientTest{
    public static void main(String[] args) throws IOException {
        ClientNetwork client = new ClientNetwork(5000, 5555, 5556, "192.168.1.32");
        // client.run();
        
        Scanner sc = new Scanner(System.in);
        
        
    }


}
