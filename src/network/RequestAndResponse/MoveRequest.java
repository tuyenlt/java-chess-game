package network.RequestAndResponse;

public class MoveRequest {
    public int stX;
    public int stY;
    public int enX;
    public int enY;

    public MoveRequest() {
    }

    public MoveRequest(int stX, int stY, int enX, int enY) {
        this.stX = stX;
        this.stY = stY;
        this.enX = enX;
        this.enY = enY;
    }

    @Override
    public String toString(){
        return stX+ "-" + stY + " move to " + enX + "-" + enY;
    }
}
