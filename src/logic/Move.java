package logic;

public class Move {
    private int startRow, startCol;
    private int endRow, endCol;

    // convert the move like e2e4 to the broad index
    Move(String strMove){
        startCol = strMove.charAt(0) - 'a'; 
        startRow = 8 - (strMove.charAt(1) - '0');
        endCol = strMove.charAt(2) - 'a';
        endRow = 8 - (strMove.charAt(3) - '0');
    }

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

    @Override 
    public String toString(){ // convert to string move, stockfish read only string move
        char startColChar = (char) ('a' + startCol);
        char endColChar = (char) ('a' + endCol);
        int startRowNum = 8 - startRow;
        int endRowNum = 8 - endRow;
        return "" + startColChar + startRowNum + endColChar + endRowNum;
    }
}
