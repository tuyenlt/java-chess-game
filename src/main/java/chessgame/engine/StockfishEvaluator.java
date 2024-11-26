package chessgame.engine;
import java.io.*;

public class StockfishEvaluator {
    private Process engine;
    private BufferedReader input;
    private BufferedWriter output;

    // Start Stockfish engine
    public StockfishEvaluator() {
        try {
            String stockfishPath = StockfishEngineDemo.class
                .getResource("/chessgame/stockfish/stockfish.exe")
                .getPath();
            stockfishPath = new File(stockfishPath).getAbsolutePath();
            engine = new ProcessBuilder(stockfishPath).start();
            input = new BufferedReader(new InputStreamReader(engine.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            // return false;
        }
    }

    // Send a command to Stockfish
    public void sendCommand(String command) throws IOException {
        output.write(command + "\n");
        output.flush();
    }

    // Get Stockfish's output with a wait time
    public String getOutput(int waitTime) {
        StringBuilder response = new StringBuilder();
        try {
            Thread.sleep(waitTime); // Wait for Stockfish to respond
            while (input.ready()) {
                response.append(input.readLine()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            engine.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String evaluateLastMove(String beforePos, String afterPos, int depth) {
        try {
            System.out.println(beforePos);
            System.out.println(afterPos);
            sendCommand("position startpos moves " + beforePos);
            sendCommand("go depth " + depth);
            String beforeEval = parseEvaluation(getOutput(1000));

            sendCommand("position startpos moves " + afterPos);
            sendCommand("go depth " + depth);
            String afterEval = parseEvaluation(getOutput(1000));

            int beforeScore = extractScore(beforeEval);
            int afterScore = extractScore(afterEval);
            boolean isMate = isMateDetected(afterEval);

            int scoreChange = afterScore - beforeScore;

            return categorizeMove(scoreChange, isMate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error evaluating move";
    }

    private int extractScore(String evaluation) {
        if (evaluation.contains("score cp")) {
            String[] parts = evaluation.split("score cp ");
            String scoreStr = parts[1].split(" ")[0];
            return Integer.parseInt(scoreStr);
        } else if (evaluation.contains("score mate")) {

            return evaluation.contains("-") ? -10000 : 10000;
        }
        return 0;
    }

    private boolean isMateDetected(String evaluation) {
        return evaluation.contains("score mate");
    }

    private String categorizeMove(int scoreChange, boolean isMate) {
        if (isMate) {
            return "Brilliant"; 
        }
        if (scoreChange >= 50) return "Best";
        if (scoreChange >= 20) return "Excellent";
        if (scoreChange >= 10) return "Good";
        if (scoreChange >= -10) return "Inaccuracy";
        if (scoreChange >= -50) return "Mistake";
        return "Blunder";
    }

    private String parseEvaluation(String stockfishOutput) {
        String[] lines = stockfishOutput.split("\n");
        for (String line : lines) {
            if (line.startsWith("info")) {
                return line;
            }
        }
        return "";
    }

    public boolean isReady() {
        try {
            sendCommand("isready");
            return getOutput(100).contains("readyok");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
