package battleships.game.dvira.battleshipsonline;

import android.os.Handler;
import android.util.Log;

/**
 * Created by dvira on 08-Aug-19.
 */

public class Game {
    Player p1;
    Player p2;
    int turn;
    CompGame g;
    public Game(Player p1, Player p2, CompGame g){
        this.p1 = p1;
        this.p2 = p2;
        turn = 0;
        this.g = g;
    }
    public void nextTurn(){
        if (turn == 0) turn = 1;
        else if (turn == 1) {
            getTurn();
            g.drawboard(p1.board);
            turn = 0;
        }
    }
    public boolean getTurn(){
        int[] selected;
        if (turn == 0) {
            selected = p1.getSelected();
            if (p2.getBoard().get(selected[0],selected[1]) == Board.SEA){
                p2.getBoard().set(selected[0],selected[1],Board.MISS);
                p1.hitSomething(selected[0],selected[1],false);
                p1.sunkAShip(selected[0], selected[1], isSunk(p2, selected[0],selected[1]));
                return true;
            }
            else if (p2.getBoard().get(selected[0],selected[1]) == Board.SHIP){
                p2.getBoard().set(selected[0],selected[1],Board.HIT);
                p1.hitSomething(selected[0],selected[1],true);
                p1.sunkAShip(selected[0], selected[1], isSunk(p2, selected[0],selected[1]));
                return true;
            }
            else return false;
        }
        else {
            selected = p2.getSelected();
            if (p1.getBoard().get(selected[0],selected[1]) == Board.SEA){
                p1.getBoard().set(selected[0],selected[1],Board.MISS);
                p2.hitSomething(selected[0],selected[1],false);
                p2.sunkAShip(selected[0], selected[1], isSunk(p1, selected[0],selected[1]));
                return true;
            }
            else if (p1.getBoard().get(selected[0],selected[1]) == Board.SHIP){
                p1.getBoard().set(selected[0],selected[1],Board.HIT);
                Log.d("D:", "In Game Hit Something");
                p2.hitSomething(selected[0],selected[1],true);
                p2.sunkAShip(selected[0], selected[1], isSunk(p1, selected[0],selected[1]));
                return true;
            }
            else return false;
        }
    }

    public boolean isSunk(Player p, int x, int y){
        return p.board.getShip(x,y) != null && p.board.isShipSunk(p.board.getShip(x,y));
    }
}
