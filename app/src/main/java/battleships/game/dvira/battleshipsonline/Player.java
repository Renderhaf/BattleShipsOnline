package battleships.game.dvira.battleshipsonline;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class Player implements Serializable {
    protected Board board;
    String name;
    public Player(Board b){
        board = b;
    }
    public Board getBoard(){
        return board;
    }
    public void setBoard(Board b){
        board = b;
    }
}
