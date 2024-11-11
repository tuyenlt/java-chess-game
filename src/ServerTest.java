import java.io.IOException;

import network.MainServer;

public class ServerTest {
    public static void main(String[] args) throws IOException{
        MainServer mainServer = new MainServer(5555, 6666);
        mainServer.run();
    }
}
