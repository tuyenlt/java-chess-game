package chessgame.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class StockfishEngine {
    private Process stockfishProcess;
    private BufferedReader stockfishReader;
    private OutputStreamWriter stockfishWriter;
    private String stockfishPath = "/stockfish/stockfish.exe"; 
    private int depth ;
    
    // Khởi chạy Stockfish
    public StockfishEngine() {
        try {
            String path = StockfishEngine.class
                    .getResource("/chessgame/stockfish/stockfish.exe")
                    .getPath().replace("\\", "/");
            stockfishPath = URLDecoder.decode(path, "UTF-8");
            System.out.println( stockfishPath);
            stockfishPath = new File(stockfishPath).getAbsolutePath();
            stockfishProcess = new ProcessBuilder(stockfishPath).start();
            stockfishReader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            stockfishWriter = new OutputStreamWriter(stockfishProcess.getOutputStream());
            depth = 10;
        } catch (IOException e) {
            e.printStackTrace();
            // return false;
        }
        // return true;
    }

    // Độ sâu phân tích tối đa là 30.
    // Tôi nghĩ lên để 3 mức là 10,20,30 tương ứng với dễ, trung bình khó.
    public void setDepth(int depth){
        this.depth=depth;
    }
    

    public void setDifficulty(String difficulty){
        switch(difficulty){
            case "Easy":
                setDepth(5);
                break;
            case "Medium":
                setDepth(10);
                break;
            case "Hard":
                setDepth(20);
                break;
            case "Very Hard":
                setDepth(30);
                break;
        }
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
                // System.out.println(line);
                return line.split(" ")[1];
            }
        }
        return null;
    }

    public String getBestMove(String moves){
        sendCommand("position startpos moves " + moves);
        return getBestMove();
    }
    
    // Trả về tỉ lệ thắng của người chơi hiện tại
    public double getWinRate(String moves, String current){
        int score = 0;
        sendCommand("position startpos moves " + moves);
        // Sao nó in ra 0???????
        sendCommand("go depth "+ depth);                 // Phân tích với độ sâu depth mặc định là 20
        // sendCommand("go depth "+ 5);                 // Phân tích với độ sâu depth mặc định là 20
        List<String> output = readOutput();
        for (String line : output) {
            if(line.startsWith("info depth " + depth)){
                // System.out.println(line);
                if (line.contains("score cp")) {
                    // Điểm dựa trên vật chất (centipawn)
                    String[] parts = line.split("score cp ");
                    score = Integer.parseInt(parts[1].split(" ")[0]);
                    if (current.equals("w") ) score = -score;
                } else if (line.contains("score mate")) {
                    score = Integer.MAX_VALUE;
                }
            } 
        }
        System.out.println(score);
        // System.out.println(allMoves.size());
        // System.out.println(score);
        // System.out.println(1/(1+Math.exp(-0.003*score)));
        
        // Chuyển hóa từ điểm centipawn sang tỉ lệ thắng
        return (1/(1+Math.exp(-0.003*score)));
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