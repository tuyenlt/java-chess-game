import java.io.IOException;

import network.ServerNetwork;

public class ServerTest {
    public static void main(String[] args) throws IOException{
        ServerNetwork server = new ServerNetwork(5555, 5556);
        server.run();
    }
}
