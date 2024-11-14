import java.io.*;

import engine.StockfishEngine;
import network.database.DatabaseConnection;
import network.packets.GeneralPackets.LoginRequest;

public class Test {

    public static void main(String[] args){
        try {    
            DatabaseConnection.DatabaseConnectionInit();
            LoginRequest req = new LoginRequest();
            req.userName = "tuyenlt";
            req.passwd = "tuyenlt";
            DatabaseConnection.loginAuthentication(req);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}