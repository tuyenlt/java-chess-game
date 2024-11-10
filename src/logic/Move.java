package logic;

public class Move {
    private int startRow, startCol;
    private int endRow, endCol;
    Move(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }
    public Move backMove(){
        return new Move(endRow, endCol, startRow, startCol);
    }
    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }
    public boolean is_castling(Board board){
        if(!(board.getPiece(startRow, startCol) instanceof King)) return false;
        if(endCol - startCol == 1)return false;
        return true;
    }
    public boolean is_promotion(Board board){
        if(!(board.getPiece(startRow, startCol) instanceof Pawn)) return false;
        return endRow == 0 || endRow == 7;
    }
}
