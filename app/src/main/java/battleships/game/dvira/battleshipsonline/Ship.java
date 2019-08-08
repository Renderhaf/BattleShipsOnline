package battleships.game.dvira.battleshipsonline;

/**
 * Created by dvira on 08-Aug-19.
 */

public class Ship {
    int[][] locations;
    int loc = 0;
    public Ship(int len){
        locations = new int[len][2];
    }
    public void addLocation(int x, int y){
        locations[loc][0] = x;
        locations[loc][1] = y;
        loc++;
    }
}
