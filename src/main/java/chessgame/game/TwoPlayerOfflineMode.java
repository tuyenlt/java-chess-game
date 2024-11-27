package chessgame.game;

public class TwoPlayerOfflineMode extends MainGame {
    public TwoPlayerOfflineMode(boolean isBoardReverse){
        super("twoPlayer", isBoardReverse);
        setPlayerTop("Black Player", "???", "b", false);
        setPlayerBottom("White Player", "???", "w", false);
    }

    @Override
    protected void handleOnMovePiece(String currentTurn) {
        // TODO Auto-generated method stub
        
    }
}
