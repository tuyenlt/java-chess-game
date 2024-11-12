package engine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockfishEngineDemo {
    private Process stockfishProcess;
    private BufferedReader stockfishReader;
    private OutputStreamWriter stockfishWriter;
    private final String stockfishPath = "stockfish\\stockfish.exe"; 
    
    // Khởi chạy Stockfish
    public boolean start() {
        try {
            stockfishProcess = new ProcessBuilder(stockfishPath).start();
            stockfishReader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            stockfishWriter = new OutputStreamWriter(stockfishProcess.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Gửi lệnh tới Stockfish
    public void sendCommand(String command) {
        try {
            stockfishWriter.write(command + "\n");
            stockfishWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Đọc phản hồi từ Stockfish
    public List<String> readOutput() {
        List<String> output = new ArrayList<>();
        try {
            String line;
            while ((line = stockfishReader.readLine()) != null ) {
                output.add(line);
                if(line.startsWith("bestmove"))break;
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Xử lý trường hợp gửi hàng loạt các nước đi từ trước tới giờ
    public void setPosition(List<String> moves) {
        sendCommand("position startpos moves " + String.join(" ", moves));
    }
    public void setPosition(String move){
        sendCommand("position startpos moves " + move);
    }
    
    // Lấy nước đi tốt nhất từ vị trí hiện tại
    public String getBestMove() {
        sendCommand("go movetime 1000");  // Phân tích trong 1 giây
        List<String> output = readOutput();
        for (String line : output) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }
    
    // Dừng Stockfish
    public void stop() {
        sendCommand("quit");
        try {
            stockfishReader.close();
            stockfishWriter.close();
            stockfishProcess.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Demo chơi với bot dưới dạng dòng lệnh
    public static void main(String[] args) {
        StockfishEngineDemo client = new StockfishEngineDemo();
        if (client.start()) {
            
            // Chơi với bot dưới dạng dòng lệnh
            Scanner sc = new Scanner(System.in);
            String move = sc.nextLine().trim();
            while(!move.equals("end")){
                client.setPosition(move);
                String bestMove = client.getBestMove();
                System.out.println("Best move: " + bestMove);
                client.setPosition(bestMove);
                move = sc.nextLine().trim();
            }
            client.stop();
        }
    }
}
