package network.database;
import network.RequestAndResponse.*;

public class DatabaseConnection {
    public static UserResponse loginAuthentication(LoginRequest loginRequest) throws Exception{

        // xử lý đăng nhập nếu DB không có userName hoặc không khớp mk
        // throws exptions
        boolean isUserExits = false;
        boolean isPasswordCorrect = false;
        
        UserResponse user = new UserResponse();

        //*xử lý database logic ở đây, nếu hợp lệ trả về thông tin user qua class UserResponse
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



    public static SimpleResponse registerNewUser(RegisterRequest registerRequest){

        boolean isUserNameExist = false; // check xem userName đã tồn tại chưa
        SimpleResponse response = new SimpleResponse();


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

    public static HistoryGame.Response getHistoryGame(HistoryGame.Request gameRequest){
        HistoryGame.Response response = new HistoryGame.Response();
        //* trả về nước đi của trận có id = gameReuest.gameId
        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */

        return response;
    }


    public static ProfileView.Response getProfile(ProfileView.Request request){
        ProfileView.Response response = new ProfileView.Response();
        //* trả về thông tin của user id = request.userID
        //*xử lý database logic ở đây
        //*
        //*
        //*
        //* */

        return response;

    }    
}
