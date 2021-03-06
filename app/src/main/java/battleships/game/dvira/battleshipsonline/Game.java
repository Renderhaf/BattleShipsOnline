package battleships.game.dvira.battleshipsonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/***
 * Created by dvira on 08-Aug-19.
 */

public class Game {
    Player p1;
    Player p2;
    int turn;
    CompGame g;
    MediaPlayer hit;
    MediaPlayer plop;
    int turnnum;
    StorageManager sm;
    boolean soundEffects;

    public Game(Player p1, Player p2, CompGame g){
        this.p1 = p1;
        this.p2 = p2;
        turn = 0;
        this.g = g;

        hit = MediaPlayer.create( g.getApplicationContext(), R.raw.boom_sound);
        plop = MediaPlayer.create( g.getApplicationContext(), R.raw.splash_sound);
        final float volume = (float) (1 - (Math.log(100 - 75) / Math.log(100)));
        plop.setVolume(volume, volume);

        sm = new StorageManager(g);
        soundEffects = sm.getBoolean("soundEffects", true);
    }

    /**
    switches the turn number between 0 and 1, and gets a turn from the auto player
     */
    public void nextTurn(){
        if (turn == 0) turn = 1;
        else if (turn == 1) {
            getTurn();
            g.drawboard(p1.board);
            turn = 0;
        }
        turnnum++;
        g.turnnumText.setText("Turn : " + turnnum);
    }

    /**
    Main function - acts according to which turn this is
     */
    public boolean getTurn(){
        int[] selected;
        if (turn == 0) { // Human Player turn
            selected = p1.getSelected();

            //tells the player he missed
            if (p2.getBoard().get(selected[0],selected[1]) == Board.SEA){
                p2.getBoard().set(selected[0],selected[1],Board.MISS);
                p1.hitSomething(selected[0],selected[1],false);
                p1.sunkAShip(selected[0], selected[1], isSunk(p2, selected[0],selected[1]));

                plopsound();
                return true;
            }

            //tells the player he hit a ship
            else if (p2.getBoard().get(selected[0],selected[1]) == Board.SHIP){
                p2.getBoard().set(selected[0],selected[1],Board.HIT);
                p1.hitSomething(selected[0],selected[1],true);
                p1.sunkAShip(selected[0], selected[1], isSunk(p2, selected[0],selected[1]));

                //inform player that ship is sunk
                if (isSunk(p2, selected[0],selected[1])) {
                    Toast.makeText(g.getApplicationContext(), "You sunk a ship!", Toast.LENGTH_SHORT).show();
                    if (isWin(p2)){
                        if (sm.getBoolean("devMode",false) || sm.getInt("shipnum", 5) != 5){
                            breakDevMode(); //breaks if not in regular mode (shipnum is not 5 or dev mode is on)
                        } else {
                            playerWin();
                        }
                    }
                    //surround the ship in missed blocks
                    p2.getBoard().surroundShipWithMissed(p2.getBoard().getShip(selected[0], selected[1]));
                }

                hitsound();
                return true;
            }
            else return false;
        }
        else { // AutoPlayers Turn
            selected = p2.getSelected();

            //Informs the player that he missed
            if (p1.getBoard().get(selected[0],selected[1]) == Board.SEA){
                p1.getBoard().set(selected[0],selected[1],Board.MISS);
                p2.hitSomething(selected[0],selected[1],false);
                p2.sunkAShip(selected[0], selected[1], isSunk(p1, selected[0],selected[1]));

                plopsound();
                return true;
            }
            //Informs the player that he hit
            else if (p1.getBoard().get(selected[0],selected[1]) == Board.SHIP){
                p1.getBoard().set(selected[0],selected[1],Board.HIT);
                p2.hitSomething(selected[0],selected[1],true);
                p2.sunkAShip(selected[0], selected[1], isSunk(p1, selected[0],selected[1]));

                //If the player sunk the last ship, inform the human player that he lost
                if (isSunk(p1, selected[0],selected[1]) && isWin(p1)){
                    playerLost();
                }
                hitsound();
                return true;
            }
            else return false;
        }
    }

    /**
    checks if a ship is sunk
     */
    public boolean isSunk(Player p, int x, int y){
        return p.board.getShip(x,y) != null && p.board.isShipSunk(p.board.getShip(x,y));
    }

    /**
    makes a ship hit sound
     */
    private void hitsound(){
        if (soundEffects)
            hit.start();
    }

    /**
    makes a ship not hit sound
     */
    private void plopsound(){
        if (soundEffects)
            plop.start();
    }

    /**
    checks if a player has won (all of the parameter players / opponents players ships are down)
     */
    private boolean isWin(Player p){
        Ship[] ships = p.getBoard().getShips();
        for (Ship ship : ships){
            if (!p.board.isShipSunk(ship)){
                return false;
            }
        }
        return true;
    }

    /**
    human player has won, show alert dialog
     */
    private void playerWin(){
        AlertDialog alertDialog = new AlertDialog.Builder(g).create();
        alertDialog.setTitle("You Won!");
        alertDialog.setMessage("\nYou have won the game after " + turnnum + " turns");
        if (sm.getInt("highscore", 1000) > turnnum){
            alertDialog.setMessage("\nNew high score! old high score was " + sm.getInt("highscore", 1000));
        } else {
            alertDialog.setMessage("\nYour high score is " + sm.getInt("highscore", 1000));
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (sm.getInt("highscore", 1000) > turnnum){
                            sm.setInt("highscore", turnnum);
                            Intent i = new Intent(g.getApplicationContext(), NewHS.class);
                            i.putExtra("score",turnnum);
                            g.startActivity(i);
                        } else {
                            Intent i = new Intent(g.getApplicationContext(), Menu.class);
                            g.startActivity(i);
                        }
                    }
                });
        alertDialog.show();
    }

    /**
    Human player has won, but in developer mode
     */
    private void breakDevMode(){
        AlertDialog alertDialog = new AlertDialog.Builder(g).create();
        alertDialog.setTitle("You Won!");
        alertDialog.setMessage("\nYou have won the game after " + turnnum + " turns");
        alertDialog.setMessage("Unfortunetly, you cannot save your score if dev mode is turned on or if Shipnum is not 5");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(g.getApplicationContext(), Menu.class);
                        g.startActivity(i);
                    }
                });
        alertDialog.show();
    }

    /**
    human player has lost
     */
    private void playerLost(){
        AlertDialog alertDialog = new AlertDialog.Builder(g).create();
        alertDialog.setTitle("You Lost!");
        alertDialog.setMessage("\nYou have lost the game after " + turnnum + " turns");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(g.getApplicationContext(), Menu.class);
                        g.startActivity(i);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CALL MOM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                g.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+sm.getString("momNumber",""))));
            }
        });
        alertDialog.show();
    }
}
