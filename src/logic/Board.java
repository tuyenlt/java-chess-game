// File này thuộc package logic
package logic;


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
    public Piece getPiece(int row, int col){
        return board[row][col].getPiece();
    }
    public void setPiece(int row, int col, Piece piece){
        board[row][col].setPiece(piece);
    }
    public int getKing_row(String color){
        if(color.equals("w")) return wK_row;
        else return bK_row;
    }
 
    public int getKing_col(String color){
        if(color.equals("w")) return wK_col;
        else return bK_col;
    }

    public void setKing_pos(String color, int row, int col){
        if(color.equals("w")){
            wK_row=row;
            wK_col=col;
        }else{
            bK_row=row;
            bK_col=col;
        }  
    } 

    public void move_piece(Move move){
        Piece piece = getPiece(move.getStartRow(), move.getStartCol());
        setPiece(move.getEndRow(), move.getEndCol(), piece);
        setPiece(move.getStartRow(), move.getStartCol(), null);
        if(piece instanceof King){
            setKing_pos(piece.getColor(), move.getEndRow(), move.getEndCol());
        }
        // thêm tham số bool isFakeMove là được
        //Đoạn này đang cần xử lý việc đánh giấu xem vua và xe đã di chuyển chưa
        //Có 1 vấn đề là ở bên Utils có ham is_safe_move nó di chuyển giả 
        //Đang không biết làm sao để không đánh dấu nếu nó là di chuyển giả  
        //King King_piece = (King)piece;
        //King_piece.setIs_move(true);
        //Phép xử lý các nước đi đặc biệt
        if(move.isCastling(this)){
            Castling();
        }
        if(move.is_promotion(this)){
            Promotion();
        }
    }
    //Nhập thành
    private void Castling (){

    }
    //Phong hậu 
    private void Promotion (){

    }

}
