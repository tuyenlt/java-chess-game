package game;

import javafx.scene.chart.PieChart;
import logic.Board;
import logic.Move;
import logic.Piece;
import logic.Utils;

public class TwoPlayerOfflineMode {
    private Board board = new Board();
    private int [] selectedSquare =null;
    public void start(){
        // Mai làm tiếp
    }

    private void handleClick(int row, int col){
        Piece piece = board.getPiece(row, col);
        if (piece ==null) return;
        if(selectedSquare == null || piece.getpieceColor().equals(board.getCurrentTurn())){
            selectPiece(row, col);
        }else{
            int startRow = selectedSquare[0];
            int startCol = selectedSquare[1];   
            Move move = new Move(startRow, startCol, row, col);

            if(board.getPiece(startRow, startCol).getSafeMoves(board, startRow, startCol).contains(move)){
                playturn(move);
            }
        }
    }
    private void selectPiece(int row,int col){
        Piece piece = board.getPiece(row, col);
        if(piece.getpieceColor().equals(board.getCurrentTurn())){
            selectedSquare = new int[]{row, col};
            // Thực hiện highlight quân cờ
        }
    }

    private void playturn(Move move){
        // Bỏ highlight
        // Di chuyển quân cờ(UI)

        // Di chuyển quân cờ(logic)
        board.movePiece(move);

    }
}
