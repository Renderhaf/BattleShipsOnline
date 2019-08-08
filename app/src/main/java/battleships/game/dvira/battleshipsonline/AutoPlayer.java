package battleships.game.dvira.battleshipsonline;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by dvira on 08-Aug-19.
 */

public class AutoPlayer extends Player implements Serializable{
    public AutoPlayer(){
        super(new Board());
        setBoard(buildBoard(5));
        Log.d("L ", board.toString());
    }

    private Board buildBoard(int amount){
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
                if (checkdir(rd, selected, lens[i])){
                    addNew(lens[i], rd, selected,b);
                    break;
                }
            }
        }
        Log.d("L", b.getShips().length+"");
        return b;
    }

    private boolean checkdir(int dir, int[] selected, int len){ // returns a boolean which states if you can lay a ship
        switch (dir){
            case 0:
                if (selected[0] + len > 10) // check walls
                    return false;

                for (int i = 0; i < len; i ++){ // check for other ships
                    if (board.get(selected[0]+i, selected[1]) == 1)
                        return false;
                }
                break;
            case 1:
                if (selected[1] + len > 10)
                    return false;

                for (int i = 0; i < len; i ++){
                    if (board.get(selected[0], selected[1]+i) == 1)
                        return false;
                }
                break;
            case 2:
                if (selected[0] - len < 0)
                    return false;

                for (int i = 0; i < len; i ++){
                    if (board.get(selected[0]-i, selected[1]) == 1)
                        return false;
                }
                break;
            case 3:
                if (selected[1] - len < 0)
                    return false;

                for (int i = 0; i < len; i ++){
                    if (board.get(selected[0], selected[1]-i) == 1)
                        return false;
                }
                break;
        }

        //check if you are around a ship

        return true;
    }

    private int getRandomLocation(){
        return (int) (10 * Math.random());
    }

    private void addNew(int len, int dir, int[] selected, Board b){
        Ship s = new Ship(len);
        switch (dir){
            case 0:
                for (int i = 0; i < len; i ++){
                    b.set(selected[0]+i, selected[1], 1);
                    s.addLocation(selected[0]+i, selected[1]);
                }
                b.addShip(s);
                return;
            case 1:
                for (int i = 0; i < len; i ++){
                    b.set(selected[0], selected[1]+i, 1);
                    s.addLocation(selected[0], selected[1]+i);
                }
                b.addShip(s);
                return;
            case 2:
                for (int i = 0; i < len; i ++){
                    b.set(selected[0]-i, selected[1], 1);
                    s.addLocation(selected[0]-i, selected[1]);
                }
                b.addShip(s);
                return;
            case 3:
                for (int i = 0; i < len; i ++){
                    b.set(selected[0], selected[1]-i, 1);
                    s.addLocation(selected[0], selected[1]-i);
                }
                b.addShip(s);
                return;
        }
    }
}
