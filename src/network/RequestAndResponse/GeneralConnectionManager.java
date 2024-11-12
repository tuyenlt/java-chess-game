package network.RequestAndResponse;

import java.util.ArrayList;

public class GeneralConnectionManager {
    public static class LoginRequest {
        public String userName;
        public String passwd;
        
        public LoginRequest(){

        }
        
        public LoginRequest(String userName, String passwd) {
            this.userName = userName;
            this.passwd = passwd;
        }  
    }
    
    public static class LoginResponse {
        public String fullName;
        public String name;
        public String passwd;
        public String avatarPath;
        public int elo;
    }

    public static class RegisterRequest {
        public String fullName;
        public String userName;
        public String passwd;

        public RegisterRequest(){

        }

        public RegisterRequest(String fullName, String userName, String passwd) {
            this.fullName = fullName;
            this.userName = userName;
            this.passwd = passwd;
        }
        
    }

    public static class RegisterResponse {
        public boolean isSuccess;
        public String message;
    }

    public static class ErrorResponse {
        public String error;
    }
    
    public static class ReplayGameRequest {
        public String gameId;
        public ReplayGameRequest(){

        }

        public ReplayGameRequest(String gameId) {
            this.gameId = gameId;
        }
        
    }

    public static class ReplayGameResponse {
        public String gameMoves;
    }

    public static class RankingListRequest {
        public String option;

        public RankingListRequest(){

        }

        public RankingListRequest(String option) {
            this.option = option;
        }
        
    }

    public static class RankingListResponse {
        public ArrayList<UserRank> rankingList;
        class UserRank{
            public String fullName;
            public int elo;
            public int win;
            public int lose;
            public int draw;
        }
    }

    public static class ProfileViewRequest{
        public String userId;
        public ProfileViewRequest(){

        }

        public ProfileViewRequest(String userId) {
            this.userId = userId;
        }
        
    }
    
    public static class ProfileViewResponse{
        public String name;
        public String avatarPath;
        public int elo;
        public int win;
        public int lose;
        public int draw;
    }

    public static class MsgPacket {
        public String msg;
        public MsgPacket(){

        }
        public MsgPacket(String msg) {
            this.msg = msg;
        }
        
    }


    public static class FindGameRequest{
        public String userId;
        public int elo;
        public FindGameRequest(){

        }
        public FindGameRequest(String userId, int elo) {
            this.userId = userId;
            this.elo = elo;
        }
        
    }

    public static class FindGameResponse{
        public int tcpPort;
        public int udpPort;
    }

}