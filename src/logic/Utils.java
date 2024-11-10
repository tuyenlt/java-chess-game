package logic;
public class Utils {
    static boolean is_empty(Board board,int row,int col){
        return board.getPiece(row, col) ==null;
    }
    static boolean is_can_next(Board board,String color, int row,int col){
        if (row <0 || row >7 || col <0 || col >7) return false;
        if (is_empty(board, row, col)) return true;
        if (board.getPiece(row, col).getColor().equals(color)){
            return false;
        }
        return true;
    }
    static boolean is_under_attack(Board board, String color, int row_piece, int col_piece){
        for(int row = 0; row<8; row++){
            for (int col = 0; col <8; col++){
                Piece piece = board.getPiece(row,col);
                if(piece.getColor().equals(color)) continue;
                if(piece.getValidMoves(board, row, col).contains(new Move(row, col, row_piece, col_piece))){
                    return true;
                }

            }
        }
        return false;
    }
    static boolean is_check(Board board, String color){
        int King_row = board.getKing_row(color);
        int King_col = board.getKing_col(color);
        return is_under_attack(board, color, King_row, King_col);
        
    }
    static boolean is_safe_move(Board board, String color, Move move){
        Piece original__Piece = board.getPiece(move.getEndRow(), move.getEndCol());
        
        //Di chuyển giả
        //Đang vướng chỗ này
        board.move_piece(move);
        boolean safe = !is_check(board, color);

        //Khôi phục lại
        board.move_piece(move.backMove());
        board.setPiece(move.getStartRow(), move.getStartCol(), original__Piece);
        return safe;
    }
}

