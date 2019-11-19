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
    int wt = 0;
    boolean shipInspect = false;

    int startx = 0;
    int starty = 0;

    public AutoPlayer(int w, int h, int shipnum){
        super(new Board());
        setBoard(buildBoard(shipnum));
        Log.d("L ", board.toString());
        this.w = w;
        this.h = h;
        picked = new int[w][h];
    }

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
        Log.d("L", b.getShips().length+"");
        return b;
    }

    private int getRandomLocation(){
        return (int) (10 * Math.random());
    }

    @Override
    public int[] getSelected(){
        Log.d("S", "Start: " + shipInspect);
        int x=0,y=0;
        boolean didSomething = false;
        if (shipInspect){
            if (shipDir != -1){
                didSomething = shipDirInspectCheck(hitx, hity);
                if (!didSomething){
                    shipDir = shipDir;
                    didSomething = shipInspectCheck(startx, starty);

                }
            }

            else{
                didSomething = shipInspectCheck(hitx,hity);
            }

            wt++;

            if (!didSomething){
                wt = 0;
                shipInspect = false;
                randomNum();
                shipDir = -1;
            }
        }
        else {
            randomNum();
        }

        Log.d("S", "End " + shipInspect);
        picked[tempx][tempy] = 1;
        int[] ns = {tempx, tempy};
        return ns;
    }

    @Override
    public void hitSomething(int x, int y, boolean h){
        if (!shipInspect){
            startx = x;
            starty = y;
        }

        if (h){
            hitx = x;
            hity = y;
            picked[x][y] = 2;
            wt = 0;
            shipInspect = true;
            Log.d("SH","SHIP HIT!" + shipInspect);
        }
    }

    @Override
    public void sunkAShip (int x, int y, boolean s) {
        if (s){
            Log.d("D:", "SHUNK A SHIP!!!");
            shipInspect = false;
        }
    }

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

    public boolean shipInspectCheck(int rr, int cc){
        boolean didSomething = false;
        if (rr != 0 && picked[rr-1][cc] == 0){ // Up
            //                System.out.println("Option 0 Picked");
            tempx = rr - 1;
            tempy = cc;
            didSomething = true;
            shipDir = 0;
        }
        else if (cc != h-1 && picked[rr][cc+1] == 0){ // Right
            //                System.out.println("Option 1 Picked");
            tempx = rr;
            tempy = cc + 1;
            didSomething = true;
            shipDir = 1;
        }
        else if (rr != w-1 && picked[rr+1][cc] == 0){ // Down
            //                System.out.println("Option 2 Picked");
            tempx = rr + 1;
            tempy = cc;
            didSomething = true;
            shipDir = 2;
        }
        else if (cc != 0 && picked[rr][cc-1] == 0){ // Left
            //                System.out.println("Option 3 Picked");
            tempx = rr;
            tempy = cc - 1;
            didSomething = true;
            shipDir = 3;
        }
        return didSomething;
    }

    public boolean shipDirInspectCheck(int rr, int cc){
        boolean didSomething = false;
        if (shipDir == 0 && rr != 0 && picked[rr-1][cc] == 0){ // Up
//                System.out.println("Option 0 Picked");
            tempx = rr - 1;
            tempy = cc;
            didSomething = true;
            shipDir = 0;
        }
        else if (shipDir == 1 && cc != h-1 && picked[rr][cc+1] == 0){ // Right
//                System.out.println("Option 1 Picked");
            tempx = rr;
            tempy = cc + 1;
            didSomething = true;
            shipDir = 1;
        }
        else if (shipDir == 2 && rr != w-1 && picked[rr+1][cc] == 0){ // Down
//                System.out.println("Option 2 Picked");
            tempx = hitx + 1;
            tempy = hity;
            didSomething = true;
            shipDir = 2;
        }
        else if (shipDir == 3 && cc != 0 && picked[rr][cc-1] == 0){ // Left
//                System.out.println("Option 3 Picked");
            tempx = rr;
            tempy = cc - 1;
            didSomething = true;
            shipDir = 3;
        }
        return didSomething;
    }
}
