package chessgame.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class StockfishEngineDemo {
    private Process stockfishProcess;
    private BufferedReader stockfishReader;
    private OutputStreamWriter stockfishWriter;
    private String stockfishPath = "/stockfish/stockfish.exe"; 
    private int depth =20;
    
    // Khởi chạy Stockfish
    public boolean start() {
        try {
            stockfishPath = StockfishEngineDemo.class
                .getResource("/chessgame/stockfish/stockfish.exe")
                .getPath();
            stockfishPath = new File(stockfishPath).getAbsolutePath();
            stockfishProcess = new ProcessBuilder(stockfishPath).start();
            stockfishReader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            stockfishWriter = new OutputStreamWriter(stockfishProcess.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Độ sâu phân tích tối đa là 30.
    // Tôi nghĩ lên để 3 mức là 10,20,30 tương ứng với dễ, trung bình khó.
    public void setDepth(int depth){
        this.depth=depth;
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
                // System.out.println(line);
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
        sendCommand("go depth "+ depth);  
        List<String> output = readOutput();
        for (String line : output) {
            if (line.startsWith("bestmove")) {
                System.out.println(line);
                return line.split(" ")[1];
            }
        }
        return null;
    }

    // Trả về điểm số của nước đi cuối cùng
    // Dương có lợi cho trắng, âm có lợi cho đen
    // 100 điểm tương đương với lợi thế 1 con tốt
    public String getMovesScore(List<String> allMoves){
        setPosition(allMoves);
        sendCommand("go depth "+depth);                 // Phân tích với độ sâu depth mặc định là 20
        List<String> output = readOutput();
        for (String line : output) {
            if(line.startsWith("info depth " + depth)){
                if (line.contains("score cp")) {
                    // Điểm dựa trên vật chất (centipawn)
                    String[] parts = line.split("score cp ");
                    int score = Integer.parseInt(parts[1].split(" ")[0]);
                    if (allMoves.size() %2 ==0 ) score = -score;
                    return "Score: "+ score;
                } else if (line.contains("score mate")) {
                    // Điểm khi có thể chiếu bí
                    String[] parts = line.split("score mate ");
                    String moves = parts[1].split(" ")[0];
                    // Mate in là trạng thái chắc chắn bị chiếu hết(nếu đánh chuẩn chỉ) sau moves lượt
                    return "Mate in: " + moves + " moves";
                }
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
}