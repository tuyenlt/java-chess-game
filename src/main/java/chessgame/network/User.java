package chessgame.network;

public class User{
    public int playerId;
    public String name;
    public int elo;
    public int win; 
    public int lose;
    public int draw;
    
    public User(int playerId, String name, int elo, int win, int lose, int draw) {
        this.playerId = playerId;
        this.name = name;
        this.elo = elo;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }
}