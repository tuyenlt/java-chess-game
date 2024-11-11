import java.io.IOException;
import java.util.Scanner;
import network.ClientNetwork;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        ClientNetwork client = new ClientNetwork(5000, 5555, 5556, "192.168.1.32");
        // client.run();
        
        Scanner sc = new Scanner(System.in);
        
        while(true){
            int stX = sc.nextInt();
            int stY = sc.nextInt();
            int enX = sc.nextInt();
            int enY = sc.nextInt();
            client.sendMove(stX, stY, enX, enY);
        }
        
    }
}
