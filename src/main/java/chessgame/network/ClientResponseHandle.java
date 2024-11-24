package chessgame.network;



import chessgame.network.packets.GeneralPackets.*;

public interface ClientResponseHandle{
    void handleLoginResponse(LoginResponse user);
    void handleRegisterResponse(RegisterResponse response);
    void handleRankingList(RankingListResponse response);
    void handleProfileView(ProfileViewResponse response);
    void handleHistoryGame(HistoryGameResponse response);
    void handleNewGameResonse(FindGameResponse response);
}