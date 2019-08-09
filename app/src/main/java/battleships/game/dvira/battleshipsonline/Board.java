package battleships.game.dvira.battleshipsonline;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dvira on 07-Aug-19.
 */

public class Board implements Serializable{
    //defining types
    final static public int SEA = 0;
    final static public int SHIP = 1;
    final static public int MISS = 2;
    final static public int HIT = 3;

    private int[][] board;
    private Ship[] ships;
    public Board(){
        board = new int[10][10];
        ships = new Ship[0];
    }

    public void set(int x, int y, int val){
        board[x][y] = val;
    }

    public int get(int x, int y){
        return board[x][y];
    }

    public void reset(){
        for (int i = 0;i < 10;i++){
            for (int j = 0; j < 10; j++){
                set(i,j,0);
            }
        }
    }

    public void addShip(Ship s){
        Ship[] newships = new Ship[ships.length+1];
        for (int i = 0 ; i < ships.length; i++){
            newships[i] = ships[i];
        }
        newships[ships.length] = s;
        ships = newships;
    }

    public Ship[] getShips(){
        return ships;
    }

    public String toString(){
        String str = "";
        for (int i = 0; i < ships.length; i++){
            str += "Ship at" + ships[i].locations[0][0] + ", " + ships[i].locations[0][1] + "\n";
        }
        return str;
    }

    public Ship getShip(int x, int y){
        for (int i = 0;i < ships.length; i++){
            for (int j = 0; j < ships[i].locations.length; j++){
                if (x == ships[i].locations[j][0] && y == ships[i].locations[j][1]){
                    return ships[i];
                }
            }
        }
        return null;
    }

    public boolean isShipSunk(Ship s){
        for (int i = 0; i < s.locations.length; i++){
            if (board[s.locations[i][0]][s.locations[i][1]] == Board.SHIP) return false;
        }
        return true;
    }

}
