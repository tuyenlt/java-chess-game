package logic;


import java.util.*;

public abstract class Piece {
    protected String color; 

    public Piece(String color) {
        this.color = color;
    }
    public String getColor(){
        return color;
    }
    
    public abstract List<Move> getValidMoves(Board board, int startRow, int startCol);

    public List<Move> getSafeMoves(Board board, int startRow, int startCol) {
        List<Move> safeMoves = new ArrayList<>(); 
        for(Move move : getValidMoves(board, startRow, startCol)){
            if(Utils.is_safe_move(board, color, move)){
                safeMoves.add(move);
            }
        }
        return safeMoves;
    }
}


class Pawn extends Piece {
    public Pawn(String color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {
        List<Move> validMoves = new ArrayList<>();
        int direction = (color.equals("w")) ? -1 : 1;

        if (Utils.is_empty(board, startRow + direction, startCol)) {
            validMoves.add(new Move(startRow, startCol, startRow + direction, startCol));
            int initialRow = (color.equals("w")) ? 6 : 1;
            if (startRow == initialRow && Utils.is_empty(board, startRow + 2 * direction, startCol)) {
                validMoves.add(new Move(startRow, startCol, startRow + 2 * direction, startCol));
            }
        }

        for (int offset : new int[]{-1, 1}) {
            int endCol = startCol + offset;
            if (Utils.is_can_next(board, color, startRow + direction, endCol) 
                && !Utils.is_empty(board, startRow + direction, endCol)) {
                validMoves.add(new Move(startRow, startCol, startRow + direction, endCol));
            }
        }

        return validMoves; 
    }
}

class Rook extends Piece {
    private boolean is_move = false;
    
    public Rook(String color) {
        super(color);
    }
    
    public boolean isMove() {
        return is_move;
    }

    public void setIs_move(boolean is_move) {
        this.is_move = is_move;
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {
        List<Move> validMoves = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        for (int [] direction : directions){
            int endRow = startRow + direction[0];
            int endCol = startCol + direction[1];
            while (Utils.is_can_next(board, color, endRow, endCol)) {
                validMoves.add(new Move(startRow, startCol, endRow, endCol));
                if (!Utils.is_empty(board, endRow, endCol)){
                    break;
                }
                endRow += direction[0];
                endCol += direction[1];
            }
        }
        return validMoves;
    }
}


class Knight extends Piece {
    public Knight(String color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {
        List<Move> validMoves = new ArrayList<>();
        int[][] directions = {{-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}};
        
        for (int [] direction : directions){
            int endRow = startRow + direction[0];
            int endCol = startCol + direction[1];
            if (Utils.is_can_next(board, color, endRow, endCol)){
                validMoves.add(new Move(startRow, startCol, endRow, endCol));
            }
        }
        return validMoves;
    }
}

class Bishop extends Piece {
    public Bishop(String color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {
        List<Move> validMoves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};

        for (int [] direction : directions){
            int endRow = startRow + direction[0];
            int endCol = startCol + direction[1];
            while (Utils.is_can_next(board, color, endRow, endCol)) {
                validMoves.add(new Move(startRow, startCol, endRow, endCol));
                if (!Utils.is_empty(board, endRow, endCol)){
                    break;
                }
                endRow += direction[0];
                endCol += direction[1];
            }
        }
        return validMoves;
    }
}

class Queen extends Piece {
    public Queen(String color) {
        super(color);
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {
        List<Move> validMoves = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1},{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};

        for (int [] direction : directions){
            int endRow = startRow + direction[0];
            int endCol = startCol + direction[1];
            while (Utils.is_can_next(board, color, endRow, endCol)) {
                validMoves.add(new Move(startRow, startCol, endRow, endCol));
                if (!Utils.is_empty(board, endRow, endCol)){
                    break;
                }
                endRow += direction[0];
                endCol += direction[1];
            }
        }
        return validMoves;
    }
}

class King extends Piece {
    private boolean is_move = false;

    public King(String color) {
        super(color);
    }

    public boolean isMove() {
        return is_move;
    }

    public void setIs_move(boolean is_move) {
        this.is_move = is_move;
    }

    @Override
    public List<Move> getValidMoves(Board board, int startRow, int startCol) {        
        List<Move> validMoves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1},{1, 1}, {1, 0}, {1, -1}, {0, -1}};
        for (int [] direction : directions){

            int endRow = startRow + direction[0];
            int endCol = startCol + direction[1];
            if (Utils.is_can_next(board, color, endRow, endCol)){
                validMoves.add(new Move(startRow, startCol, endRow, endCol));
            }
        }
        if(is_LeftCastle(board, startRow, startCol)){
            validMoves.add(new Move(startRow, startCol, startRow, 2));
        }
        if (is_RightCastle(board, startRow, startCol)) {
            validMoves.add(new Move(startRow, startCol, startRow, 6));
        }
        return validMoves;
    }
    
    //Chỗ này là để xử lý logic hoán thành
    private boolean is_LeftCastle(Board board, int startRow, int startCol){
        int initialRow = ((color.equals("w")) ? 7 : 0);
        if(is_move||Utils.is_check(board, color)) return false;
        Piece piece = board.getPiece(initialRow, 0);
        if(!(piece instanceof Rook) || !piece.getColor().equals(color)) return false;
        Rook rook =(Rook) piece;
        if(rook.isMove()) return false;
        for(int col= 1; col < 4; col++){
            if(!Utils.is_empty(board, initialRow, col) || Utils.is_under_attack(board, color, initialRow, col)){
                return false;
            }
        }
        return true;
    }

    private boolean is_RightCastle(Board board, int startRow, int startCol){
        int initialRow = ((color.equals("w")) ? 7 : 0);
        if(is_move || Utils.is_check(board, color)) return false;
        Piece piece = board.getPiece(initialRow, 7);
        if(!(piece instanceof Rook) || !piece.getColor().equals(color)) return false;
        Rook rook =(Rook) piece;
        if(rook.isMove()) return false;
        for(int col= 5; col < 7; col++){
            if(!Utils.is_empty(board, initialRow, col)|| Utils.is_under_attack(board, color, initialRow, col)){
                return false;
            }
        }
        return true;
    }
}
