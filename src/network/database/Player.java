package network.database;

public class Player {
    public int playerId;
    public String name;
    public int elo;
    public int win; 
    public int lose;
    public int draw;
    
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
    
    public void gameEndWith(Player other, double score){
        double expectedScoreA = 1.0 / (1 + Math.pow(10, (other.elo - this.elo) / 400.0));
        int newElo = (int) Math.round(this.elo + 32 * (score - expectedScoreA));
        this.elo = newElo;
        if(score == 1){
            win++;
        }
        if(score == 0.5){
            draw++;
        }
        if(score == 0){
            lose++;
        }
    }
}