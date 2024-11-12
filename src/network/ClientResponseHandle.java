package network;


import network.RequestAndResponse.GeneralConnectionManager.*;

public interface ClientResponseHandle{
    void handleLoginSuccess(LoginResponse user);
    void handleLoginFail(ErrorResponse error);
    void handleRegisterResponse(RegisterResponse response);
    void handleRegister(MsgPacket response);
    void handleRankingList(RankingListResponse response);
    void handleProfileView(ProfileViewResponse response);
    void handleReplayGame(ReplayGameResponse response);
}