package battleships.game.dvira.battleshipsonline;

import java.util.ArrayList;

/**
 * Created by dvira on 07-Aug-19.
 */

public class Board {
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

}
