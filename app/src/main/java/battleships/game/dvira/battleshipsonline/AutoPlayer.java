package battleships.game.dvira.battleshipsonline;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class AutoPlayer extends Player implements Serializable{
    //vars for Auto
    int w, h;
    int[][] picked;

    private int tempx = 0;
    private int tempy = 0;

    private int hitx = 0;
    private int hity = 0;
    private int shipDir = -1;
    private boolean shipInspect = false;

    private int startx = 0;
    private int starty = 0;

    public AutoPlayer(int w, int h, int shipnum){
        super(new Board());
        setBoard(buildBoard(shipnum));
        this.w = w;
        this.h = h;
        picked = new int[w][h];
    }

    //This function creates a new Random board that complies with the game rules
    private Board buildBoard(int shipnum){
        int amount = shipnum;
        if (amount > 5) amount = 5;
        int[] lens = {3,3,5,2,1};
        Board b = new Board();

        int rx=0, ry=0, rd=0;
        int[] selected = new int[2];
        for (int i = 0; i < amount; i++){
            while (true){
                rx = getRandomExistingLocation();
                ry = getRandomExistingLocation();
                selected[0] = rx;
                selected[1] = ry;
                rd = (int) (4 * Math.random());
                if (b.checkShipDir(rd, selected, lens[i])){
                    b.addNewShipFromVals(lens[i],rd,selected);
                    break;
                }
            }
        }
        return b;
    }

    //This function supplies a random location on the board
    private int getRandomExistingLocation(){
        return (int) (10 * Math.random());
    }

    //The Game uses this function to receive the position that the player has selected (This is overridden from the Player class).
    @Override
    public int[] getSelected(){
        boolean didSomething = false;

        if (shipInspect){ //Whether it is currently in the process of destroying a ship
            if (shipDir != -1){ //If it already has a direction
                //keep going in that direction
                didSomething = shipInspect(hitx, hity);
                //If it ended that direction, then find a new direction from the starting point
                if (!didSomething){
                    didSomething = initShipInspect(startx, starty);
                }
            } else{ //If it doesn't have a direction already, find one
                didSomething = initShipInspect(hitx,hity);
            }

            //If there is nothing to do, start from a new random direction - the code should not enter the statment
            if (!didSomething){
                getClearRandomLocation();
            }
        }
        else { // if you are not looking at a ship, start looking for a new one
            getClearRandomLocation();
        }

        //save that location as a checked location
        picked[tempx][tempy] = 1;
        int[] ns = {tempx, tempy};
        return ns;
    }

    //This function is called when the player hits a ship
    @Override
    public void hitSomething(int x, int y, boolean h){
        if (h){
            if (!shipInspect){ //If you are not checking the ship you hit yet, start checking it
                startx = x;
                starty = y;
            }
            hitx = x;
            hity = y;
            picked[x][y] = 2;
            shipInspect = true;
        }
    }

    //This function is called when the player sunk a ship
    @Override
    public void sunkAShip (int x, int y, boolean s) {
        if (s){ //stop checking the ship
            shipInspect = false;
        }
    }

    //This function puts random index in tempx and tempy that are not checked yet
    public void getClearRandomLocation(){
        boolean end = false;
        while (!end){
            tempx = getRandomExistingLocation();
            tempy = getRandomExistingLocation();

            if (picked[tempx][tempy] == 0){
                end = true;
            }
        }
        shipDir = -1;
        shipInspect = false;
    }

    //This function finds an initial direction for a ship inspection
    public boolean initShipInspect(int x, int y){
        boolean didSomething = false;
        if (x != 0 && picked[x-1][y] == 0){ // Up
            tempx = x - 1;
            tempy = y;
            didSomething = true;
            shipDir = 0;
        }
        else if (y != h-1 && picked[x][y+1] == 0){ // Right
            tempx = x;
            tempy = y + 1;
            didSomething = true;
            shipDir = 1;
        }
        else if (x != w-1 && picked[x+1][y] == 0){ // Down
            tempx = x + 1;
            tempy = y;
            didSomething = true;
            shipDir = 2;
        }
        else if (y != 0 && picked[x][y-1] == 0){ // Left
            tempx = x;
            tempy = y - 1;
            didSomething = true;
            shipDir = 3;
        }
        return didSomething;
    }

    //Uses the ship direction that's already decided and keeps going in that direction unless it hits a wall or an already checked block
    //Returns whether it kept going or not
    public boolean shipInspect(int x, int y){
        boolean didSomething = false;
        if (shipDir == 0 && x != 0 && picked[x-1][y] == 0){ // Up
            tempx = x - 1;
            tempy = y;
            didSomething = true;
        }
        else if (shipDir == 1 && y != h-1 && picked[x][y+1] == 0){ // Right
            tempx = x;
            tempy = y + 1;
            didSomething = true;
        }
        else if (shipDir == 2 && x != w-1 && picked[x+1][y] == 0){ // Down
            tempx = hitx + 1;
            tempy = hity;
            didSomething = true;
        }
        else if (shipDir == 3 && y != 0 && picked[x][y-1] == 0){ // Left
            tempx = x;
            tempy = y - 1;
            didSomething = true;
        }
        return didSomething;
    }
}
