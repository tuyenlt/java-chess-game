package network.RequestAndResponse;

public class ProfileView {
    public static class Request{
        public String userId;
    }
    
    public static class Response{
        public UserResponse user;
    }
}
