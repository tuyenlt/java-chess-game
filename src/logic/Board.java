package logic;

import java.util.ArrayList;
import java.util.List;
class Box {
    private Piece piece;

    Box(Piece piece) {
        this.piece = piece;
    }

    Box() {
        this.piece = null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}

public class Board {
    private Box[][] board;
    
    //Thêm danh sách lịch sử di chuyển của 2 bên để tiện sử dụng sau này
    private List<Move> white_moves = new ArrayList<>();
    private List<Move> black_moves = new ArrayList<>();

    private int wK_row, wK_col;
    private int bK_row, bK_col;
    Board() {
        board = createBoard();
        wK_row = 7;
        wK_col = 4;
        bK_row = 0;
        bK_col = 4;
    }

    private Box[][] createBoard() {
        Box[][] initialBoard = new Box[8][8];

        // Khởi tạo hàng 1 với quân đen
        initialBoard[0][0] = new Box(new Rook("b"));
        initialBoard[0][1] = new Box(new Knight("b"));
        initialBoard[0][2] = new Box(new Bishop("b"));
        initialBoard[0][3] = new Box(new Queen("b"));
        initialBoard[0][4] = new Box(new King("b"));
        initialBoard[0][5] = new Box(new Bishop("b"));
        initialBoard[0][6] = new Box(new Knight("b"));
        initialBoard[0][7] = new Box(new Rook("b"));

        for (int i = 0; i < 8; i++) {
            initialBoard[1][i] = new Box(new Pawn("b"));
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                initialBoard[i][j] = new Box();
            }
        }

        for (int i = 0; i < 8; i++) {
            initialBoard[6][i] = new Box(new Pawn("w"));
        }

        initialBoard[7][0] = new Box(new Rook("w"));
        initialBoard[7][1] = new Box(new Knight("w"));
        initialBoard[7][2] = new Box(new Bishop("w"));
        initialBoard[7][3] = new Box(new Queen("w"));
        initialBoard[7][4] = new Box(new King("w"));
        initialBoard[7][5] = new Box(new Bishop("w"));
        initialBoard[7][6] = new Box(new Knight("w"));
        initialBoard[7][7] = new Box(new Rook("w"));

        return initialBoard;
    }

    // Lấy thông tin quân cờ
    public Piece getPiece(int row, int col){
        return board[row][col].getPiece();
    }

    // Thiết lập quân cờ
    public void setPiece(int row, int col, Piece piece){
        board[row][col].setPiece(piece);
    }

    // Lấy thông tin vị trí của King
    public int getKingRow(String pieceColor){
        if(pieceColor.equals("w")) return wK_row;
        else return bK_row;
    }
 
    public int getKingCol(String pieceColor){
        if(pieceColor.equals("w")) return wK_col;
        else return bK_col;
    }

    // Thiết lập vị trí của King
    public void setKing_pos(String pieceColor, int row, int col){
        if(pieceColor.equals("w")){
            wK_row=row;
            wK_col=col;
        }else{
            bK_row=row;
            bK_col=col;
        }  
    }

    // Thực hiện di chuyển quân cờ
    public void movePiece(Move move){
        movePiece(move, false);
    }

    public void movePiece(Move move, boolean isFakeMove){
        //Thực hiện di chuyển
        Piece piece = getPiece(move.getStartRow(), move.getStartCol());
        setPiece(move.getEndRow(), move.getEndCol(), piece);
        setPiece(move.getStartRow(), move.getStartCol(), null);
        
        //Đánh dấu trạng thái di chuyển của King và Rook
        if(piece instanceof King){
            setKing_pos(piece.getpieceColor(), move.getEndRow(), move.getEndCol());
            ((King)piece).setHasMoved(true);
        }
        if(piece instanceof Rook){
            ((Rook)piece).setHasMoved(true);
        }
        
        //Phép xử lý các nước đi đặc biệt
        if(move.isCastling(this)){
            Castling(move);
        }
        if(move.isPromotion(this)){
            Promotion(move.getEndRow(),move.getEndCol(),piece.getpieceColor());
        }

        //Thêm phép di chuyển vào danh sách
        if(isFakeMove) return;
        if(piece.getpieceColor().equals("w")){
            white_moves.add(move);
        }else{
            black_moves.add(move);
        }
    }
         
    // Xử lý riêng phần nhập thành
    private void Castling (Move move){
        if(move.getEndCol() == 2){
            movePiece(new Move(move.getStartRow(),0,move.getEndRow(),3),true);
        }else{
            movePiece(new Move(move.getStartRow(),7,move.getEndRow(),5),true);
        }
    }

    // Xử lý riêng phần phong hậu
    // Phần này nếu muốn có thể nâng cấp lên thành chon 1 trong 4 quân xe, mã, tịnh, hậu.
    private void Promotion (int row, int col, String pieceColor){
        setPiece(row,col,new Queen(pieceColor));
    }

}
