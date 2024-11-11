package network.RequestAndResponse;

import java.util.ArrayList;

public class RankingListResponse {
    public ArrayList<UserRank> rankingList;
    class UserRank{
        public String fullName;
        public int elo;
        public int win;
        public int lose;
        public int draw;
    }
}
