package network.RequestAndResponse;

public class FindGame {

    public static class Request{
        public String userId;
        public int elo;
    }

    public static class Response{
        public int tcpPort;
        public int udpPort;
    }
}
