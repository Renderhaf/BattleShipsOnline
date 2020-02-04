package battleships.game.dvira.battleshipsonline;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class AutoPlayer extends Player implements Serializable{
    //vars for Auto
    int w, h;
    int[][] picked;

    int tempx = 0;
    int tempy = 0;

    int hitx = 0;
    int hity = 0;
    int shipDir = -1;
    boolean shipInspect = false;

    int startx = 0;
    int starty = 0;

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
                rx = getRandomLocation();
                ry = getRandomLocation();
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
    private int getRandomLocation(){
        return (int) (10 * Math.random());
    }

    //The Game uses this function to receive the position that the player has selected (This is overridden from the Player class).
    @Override
    public int[] getSelected(){
        boolean didSomething = false;

        if (shipInspect){ //Whether it is currently in the procces of destroying a ship
            if (shipDir != -1){ //If it already has a direction
                //keep going in that direction
                didSomething = shipDirInspectCheck(hitx, hity);
                //If it ended that direction, then find a new direction from the starting point
                if (!didSomething){
                    didSomething = shipInspectCheck(startx, starty);
                }
            } else{ //If it doesnt have a direction already, find one
                didSomething = shipInspectCheck(hitx,hity);
            }

            //If there is nothing to do, start from a new random direction - the code should not enter the statment
            if (!didSomething){
                shipInspect = false;
                randomNum();
                shipDir = -1;
            }
        }
        else { // if you are not looking at a ship, start looking for a new one
            randomNum();
        }

        //save that location as a checked location
        picked[tempx][tempy] = 1;
        int[] ns = {tempx, tempy};
        Log.d("d:  ", ns[0] + " " + ns[1]);
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
    public void randomNum(){
        boolean end = false;
        while (!end){
            tempx = ((int) (Math.random()*10));;
            tempy = ((int) (Math.random()*10));

            if (picked[tempx][tempy] == 0){
                end = true;
            }
        }
    }

    //This function finds an initial direction for a ship inspection
    public boolean shipInspectCheck(int rr, int cc){
        boolean didSomething = false;
        if (rr != 0 && picked[rr-1][cc] == 0){ // Up
            tempx = rr - 1;
            tempy = cc;
            didSomething = true;
            shipDir = 0;
        }
        else if (cc != h-1 && picked[rr][cc+1] == 0){ // Right
            tempx = rr;
            tempy = cc + 1;
            didSomething = true;
            shipDir = 1;
        }
        else if (rr != w-1 && picked[rr+1][cc] == 0){ // Down
            tempx = rr + 1;
            tempy = cc;
            didSomething = true;
            shipDir = 2;
        }
        else if (cc != 0 && picked[rr][cc-1] == 0){ // Left
            tempx = rr;
            tempy = cc - 1;
            didSomething = true;
            shipDir = 3;
        }
        return didSomething;
    }

    //Uses the ship direction thats already decided and keeps going in that direction unless it hits a wall or an already checked block
    //Returns whether it kept going or not
    public boolean shipDirInspectCheck(int rr, int cc){
        boolean didSomething = false;
        if (shipDir == 0 && rr != 0 && picked[rr-1][cc] == 0){ // Up
            tempx = rr - 1;
            tempy = cc;
            didSomething = true;
        }
        else if (shipDir == 1 && cc != h-1 && picked[rr][cc+1] == 0){ // Right
            tempx = rr;
            tempy = cc + 1;
            didSomething = true;
        }
        else if (shipDir == 2 && rr != w-1 && picked[rr+1][cc] == 0){ // Down
            tempx = hitx + 1;
            tempy = hity;
            didSomething = true;
        }
        else if (shipDir == 3 && cc != 0 && picked[rr][cc-1] == 0){ // Left
            tempx = rr;
            tempy = cc - 1;
            didSomething = true;
        }
        return didSomething;
    }
}
