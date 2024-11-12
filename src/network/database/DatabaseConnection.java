package network.database;
import network.RequestAndResponse.GeneralConnectionManager.*;

public class DatabaseConnection {
    public static LoginResponse loginAuthentication(LoginRequest loginRequest) throws Exception{

        // xử lý đăng nhập nếu DB không có userName hoặc không khớp mk
        // throws exptions
        boolean isUserExits = false;
        boolean isPasswordCorrect = false;
        
        LoginResponse user = new LoginResponse();

        //*xử lý database logic ở đây, nếu hợp lệ trả về thông tin user qua class LoginResponse
        //*
        //*
        //*
        //* */


        if(!isUserExits){
            throw new Exception("User not exit");
        }

        if(!isPasswordCorrect){
            throw new Exception("Wrong password try again");
        }

        return user;
    }



    public static MsgPacket registerNewUser(RegisterRequest registerRequest){

        boolean isUserNameExist = false; // check xem userName đã tồn tại chưa
        MsgPacket response = new MsgPacket();


        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */



        if(isUserNameExist){
            response.msg = "User name already exist";
        }else{
            response.msg = "Create new account success, go back to login";
        }

        return response;
    }


    public static RankingListResponse getRankingList(RankingListRequest rankingListRequest){
        RankingListResponse rankingListResponse = new RankingListResponse();
        //* trả về danh sách xếp hạng dựa trên elo hoặc dựa trên rankingListRequest.option
        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */


        return rankingListResponse;
    }

    public static ReplayGameResponse getHistoryGame(ReplayGameRequest gameRequest){
        ReplayGameResponse response = new ReplayGameResponse();
        //* trả về nước đi của trận có id = gameReuest.gameId
        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */

        return response;
    }


    public static ProfileViewResponse getProfile(ProfileViewRequest request){
        ProfileViewResponse response = new ProfileViewResponse();
        //* trả về thông tin của user id = request.userID
        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */

        return response;

    }    
}
