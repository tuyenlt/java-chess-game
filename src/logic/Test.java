package logic;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Move> a = new ArrayList<>();
        a.add(new Move(1,2,3,4));
        a.add(new Move(5,6,7,8));
        System.out.println(a.contains(new Move(1,2,3,4)));
    }   
}
