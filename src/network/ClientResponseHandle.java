package network;

import network.RequestAndResponse.ErrorResponse;
import network.RequestAndResponse.HistoryGame;
import network.RequestAndResponse.HistoryGame.Response;
import network.RequestAndResponse.ProfileView;
import network.RequestAndResponse.RankingListResponse;
import network.RequestAndResponse.SimpleResponse;
import network.RequestAndResponse.UserResponse;

public interface ClientResponseHandle{
    void handleLoginSuccess(UserResponse user);
    void handleLoginFail(ErrorResponse error);
    void handleRegister(SimpleResponse response);
    void handleRankingList(RankingListResponse response);
    void handleProfileView(ProfileView.Response response);
    void handleHistoryGame(HistoryGame.Response response);
}


class Example implements ClientResponseHandle{

    @Override
    public void handleHistoryGame(Response response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleLoginFail(ErrorResponse error) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleLoginSuccess(UserResponse user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleProfileView(ProfileView.Response response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleRankingList(RankingListResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleRegister(SimpleResponse response) {
        // TODO Auto-generated method stub
        
    }

}