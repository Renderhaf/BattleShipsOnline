package battleships.game.dvira.battleshipsonline;

import android.util.Log;

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

    public boolean checkShipDir(int dir, int[] selected, int len){ // returns a boolean which states if you can lay a ship
        int x,y;
        int xmul, ymul;
//        switch (dir){
//            case 0:
//                if (selected[0] + len > 10) // check walls
//                    return false;
//
//                for (int i = 0; i < len; i ++){ // check for other ships
//                    if (get(selected[0]+i, selected[1]) == 1)
//                        return false;
//
//                    if (inRange(selected[0]))
//                }
//                break;
//            case 1:
//                if (selected[1] + len > 10)
//                    return false;
//
//                for (int i = 0; i < len; i ++){
//                    if (get(selected[0], selected[1]+i) == 1)
//                        return false;
//                }
//                break;
//            case 2:
//                if (selected[0] - len < 0)
//                    return false;
//
//                for (int i = 0; i < len; i ++){
//                    if (get(selected[0]-i, selected[1]) == 1)
//                        return false;
//                }
//                break;
//            case 3:
//                if (selected[1] - len < 0)
//                    return false;
//
//                for (int i = 0; i < len; i ++){
//                    if (get(selected[0], selected[1]-i) == 1)
//                        return false;
//                }
//                break;
//        }

        switch (dir){
            case 0:
                xmul = 1;
                ymul = 0;

                if (selected[0] + len > 10) // check walls
                    return false;
                break;

            case 1:
                xmul = 0;
                ymul = 1;

                if (selected[1] + len > 10)
                    return false;
                break;
            case 2:
                xmul = -1;
                ymul = 0;

                if (selected[0] - len < 0)
                    return false;
                break;
            case 3:
                xmul = 0;
                ymul = -1;

                if (selected[1] - len < 0)
                    return false;
                break;
            default:
                //should not get here;
                xmul = 0;
                ymul = 0;
                break;
        }

        for (int i = 0; i < len; i ++){ // check for other ships
            x = selected[0]+(i*xmul);
            y = selected[1]+(i*ymul);

            if (!checkIfBlockIsGood(x,y)) return false;
        }

        return true;
    }

    private boolean inRange(int x, int y){
        return !(x>=board.length || x<0 || y>=board[0].length || y<0);
    }

    private boolean checkIfBlockIsGood(int x, int y){

        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (inRange(x+i,y+j) && board[x+i][y+j] == Board.SHIP) return false;
            }
        }

        return true;
    }

    public void surroundShipWithMissed(Ship s){
        int[][] locs = s.locations;
        for (int[] loc : locs){
            for (int i = -1; i<=1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (inRange(loc[0]+i, loc[1]+j) && board[loc[0]+i][loc[1]+j] == Board.SEA){
                        board[loc[0]+i][loc[1]+j] = Board.MISS;
                    }
                }
            }
        }
    }

    public void addNewShipFromVals(int len, int dir, int[] selected){
        Ship s = new Ship(len);
        switch (dir){
            case 0:
                for (int i = 0; i < len; i ++){
                    set(selected[0]+i, selected[1], Board.SHIP);
                    s.addLocation(selected[0]+i, selected[1]);
                }
                addShip(s);
                return;
            case 1:
                for (int i = 0; i < len; i ++){
                    set(selected[0], selected[1]+i, Board.SHIP);
                    s.addLocation(selected[0], selected[1]+i);
                }
                addShip(s);
                return;
            case 2:
                for (int i = 0; i < len; i ++){
                    set(selected[0]-i, selected[1], Board.SHIP);
                    s.addLocation(selected[0]-i, selected[1]);
                }
                addShip(s);
                return;
            case 3:
                for (int i = 0; i < len; i ++){
                    set(selected[0], selected[1]-i, Board.SHIP);
                    s.addLocation(selected[0], selected[1]-i);
                }
                addShip(s);
                return;
        }
    }
}
