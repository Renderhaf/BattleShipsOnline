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

    //returns the players board
    public Board getBoard(){
        return board;
    }

    //set the players board
    public void setBoard(Board b){
        board = b;
    }

    //gets the currently selected location for this player
    public int[] getSelected(){
        return selected;
    }

    //sets the currently selected location for this player
    public void setSelected(int[] ns){
        selected = ns;
    }

    //is called if the player hits a ship
    public void hitSomething(int x, int y, boolean h){
    }

    //is called if the player sinks a ship
    public void sunkAShip(int x, int y, boolean s){
    }
}
