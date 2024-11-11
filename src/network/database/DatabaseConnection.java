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
            throw new Exception("Password is incorrect");
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


    
}
