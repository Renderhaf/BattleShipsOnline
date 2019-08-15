package battleships.game.dvira.battleshipsonline;

import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class Player implements Serializable {
    protected Board board;
    private int[] selected;
    String name;

    public Player(Board b){
        board = b;
        selected = new int[2];
    }
    public Board getBoard(){
        return board;
    }

    public void setBoard(Board b){
        board = b;
    }

    public int[] getSelected(){
        return selected;
    }

    public void setSelected(int[] ns){
        selected = ns;
    }

    public void hitSomething(int x, int y, boolean h){
        //ship hit sound
    }

    public void sunkAShip(int x, int y, boolean s){
        //ship sunk sound
    }
}
