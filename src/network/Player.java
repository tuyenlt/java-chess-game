package network;

public class Player {
    private int playerId;
    private String name;
    private int elo;
    private int win; 
    private int lose;
    private int draw;
    
    public Player(int playerId, String name, int elo, int win, int lose, int draw) {
        this.playerId = playerId;
        this.name = name;
        this.elo = elo;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }
    
    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public int getElo() {
        return elo;
    }
    
    public void onEndGameChange(){
        
    }
    
}