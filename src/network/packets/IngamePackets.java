package network.packets;

public class IngamePackets {
    public static class GameStateResponse {
        public int timeWhite;
        public int timeBlack;
        public String nextTurn;
        public String lastMove;
    }

    public static class MovePacket{
        public int stX;
        public int stY;
        public int enX;
        public int enY;

        public MovePacket(){
            
        }

        public MovePacket(int stX, int stY, int enX, int enY) {
            this.stX = stX;
            this.stY = stY;
            this.enX = enX;
            this.enY = enY;
        }
    }

    public static class GameEndResponse{
        public String winner;
        public int TotalsMoves;
        public int eloChange;
    }
}
