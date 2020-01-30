package battleships.game.dvira.battleshipsonline;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class Ship implements Serializable{
    int[][] locations;
    int loc = 0;
    public Ship(int len){
        locations = new int[len][2];
    }

    //Adds a loction for the ship
    public void addLocation(int x, int y){
        locations[loc][0] = x;
        locations[loc][1] = y;
        loc++;
    }
}
