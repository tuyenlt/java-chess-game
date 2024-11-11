package network.RequestAndResponse;

public class FindGame {
    
    public static class Request{
        public int elo;
    }

    public static class Response{
        public String address;
        public int tcpPort;
        public int udpPort;
    }
}
