package chessgame.game;

public class TwoPlayer extends MainGame {
    public TwoPlayer(boolean isBoardReverse){
        super("twoPlayer", isBoardReverse);
        setPlayerTop("Black Player", "???", "b");
        setPlayerBottom("White Player", "???", "w");
    }

    @Override
    protected void handleOnMovePiece(String currentTurn) {
        // TODO Auto-generated method stub
        
    }
}
