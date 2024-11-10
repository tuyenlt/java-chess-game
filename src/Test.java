import java.io.*;

import engine.StockfishEngine;


public class Test {

    public static void main(String[] args) throws IOException {
        StockfishEngine stockfish = new StockfishEngine();
        stockfish.startNewGame();
        stockfish.makePlayerMove("e2e4");
        int n = 10;
        while(n-->0){
            System.out.println(stockfish.getBoardState());
            String bestMove = stockfish.getBestMove();
            System.out.println(bestMove);
            System.out.println(stockfish.getCurrentFen());
            stockfish.makePlayerMove(bestMove);
        }
        stockfish.close();
    }
}