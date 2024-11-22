package chessgame.engine;
import java.io.*;

public class StockfishEvaluator {

    private Process stockfishProcess;
    private BufferedReader reader;
    private BufferedWriter writer;

    public StockfishEvaluator(String stockfishPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(stockfishPath);
        stockfishProcess = processBuilder.start();
        reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(stockfishProcess.getOutputStream()));
    }

    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    public String readResponse() throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("readyok") || line.startsWith("bestmove")) {
                break;
            }
            response.append(line).append("\n");
        }
        return response.toString();
    }

    public String evaluatePosition(String fen, int depth) throws IOException {
        sendCommand("uci");
        readResponse();

        sendCommand("position fen " + fen);
        sendCommand("go depth " + depth);

        String response = readResponse();
        return response;
    }

    public void close() throws IOException {
        sendCommand("quit");
        reader.close();
        writer.close();
        stockfishProcess.destroy();
    }

    public static void main(String[] args) {
        try {
            String stockfishPath = "src\\main\\resources\\chessgame\\stockfish.exe"; // Đường dẫn tới Stockfish
            StockfishEvaluator evaluator = new StockfishEvaluator(stockfishPath);
            
            String fen = "rnbqkb1r/pppppppp/7n/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 0 1"; // Ví dụ FEN
            int depth = 15;

            String evaluation = evaluator.evaluatePosition(fen, depth);
            System.out.println("Evaluation Result:\n" + evaluation);

            evaluator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
