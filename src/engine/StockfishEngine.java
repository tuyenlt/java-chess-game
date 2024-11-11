package engine;

import java.io.*;

public class StockfishEngine {

    private Process stockfishProcess;
    private BufferedReader reader;
    private PrintWriter writer;
    private int depth = 1;
    private StringBuilder movesHistory = new StringBuilder();
    private String currentFen = "";

    public StockfishEngine() throws IOException {
        // change path to stock fish here
        stockfishProcess = new ProcessBuilder("E:\\TuyenLt\\Java\\chessgame\\stockfish\\stockfish.exe").start();
        reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
        writer = new PrintWriter(stockfishProcess.getOutputStream(), true);
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public void startNewGame() {
        writer.println("ucinewgame");
        writer.println("position startpos");
    }

    public void makePlayerMove(String move) {
        movesHistory.append(move + " ");
        writer.println("position startpos moves " + movesHistory.toString());
        System.out.println("position startpos moves " + movesHistory.toString());
    }

    public String getBestMove() throws IOException {
        writer.println("go depth " + depth);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }

    public String getBoardState() {
        writer.println("d");
        StringBuilder boardState = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                boardState.append(line).append("\n");
                if (line.contains("Fen: ")){
                    currentFen = line.substring(5);
                }
                if (line.contains("Checkers:")){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return boardState.toString();
    }

    public String getCurrentFen(){
        return currentFen;
    }

    public void close() {
        writer.println("quit");
        try {
            reader.close();
            writer.close();
            stockfishProcess.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
