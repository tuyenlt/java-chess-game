package network;


import network.packets.GeneralPackets.*;

public interface ClientResponseHandle{
    void handleLoginSuccess(LoginResponse user);
    void handleLoginFail(ErrorResponse error);
    void handleRegisterResponse(RegisterResponse response);
    void handleRegister(MsgPacket response);
    void handleRankingList(RankingListResponse response);
    void handleProfileView(ProfileViewResponse response);
    void handleHistoryGame(HistoryGameResponse response);
}