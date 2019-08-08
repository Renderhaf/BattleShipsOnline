package battleships.game.dvira.battleshipsonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MakeBoard extends AppCompatActivity implements View.OnClickListener{

    ImageButton[][] places;
    Button homebutton;
    Button setbtn;
    int dir;
    int len;
    int[] selected;
    Board board;
    int shipnum;
    int endships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_board);

        places = new ImageButton[10][10];
        homebutton = (Button) findViewById(R.id.btnhome);
        homebutton.setOnClickListener(this);
        setbtn = (Button) findViewById(R.id.setbtn);
        setbtn.setOnClickListener(this);

        shipnum = 0;
        dir = 0;
        len = 3;
        endships = 1;
        selected = new int[2];
        board = new Board();

        String str;
        int resID;

        for (int i = 0 ; i < 10; i ++){
            for (int j = 0; j < 10; j++){
                str = "imgBtn" + i + j;
                resID = getResources().getIdentifier(str, "id", getPackageName());
                places[i][j] = (ImageButton) findViewById(resID);
                places[i][j].setOnClickListener(this);
            }
        }
    }

    public void onClick(View v){
        if (v.getId() == homebutton.getId()){
            Intent i = new Intent(MakeBoard.this, battleships.game.dvira.battleshipsonline.Menu.class);
            startActivity(i);
        }
        for (int i = 0 ; i < 10; i ++){
            for (int j = 0; j < 10; j++){
                if (places[i][j].getId() == v.getId()){
                    if (!(selected[0]==i && selected[1]==j)) {
                        selected[0] = i;
                        selected[1] = j;
                        dir = 0;
                        places[i][j].setImageResource(R.drawable.red);
                    } else {
                        dir = (dir + 1) % 4;
                    }
                    drawnewship();
                }
            }
        }
        if (v.getId() == setbtn.getId()){
            if (selected[0] == -1 && selected[1] == -1){
                return;
            }
            addNew();
            selected[0] = -1;
            selected[1] = -1;
            dir = 0;
            drawboard();
            shipnum ++;

            if (shipnum == endships){
                AlertDialog alertDialog = new AlertDialog.Builder(MakeBoard.this).create();
                alertDialog.setTitle("Starting Game...");
                alertDialog.setMessage("You have set your board up with " + shipnum + " ships, starting game...");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Player p = new Player(board);
                                Intent i = new Intent(MakeBoard.this, CompGame.class);
                                i.putExtra("player", p);
                                startActivity(i);
                            }
                        });
                alertDialog.show();
            }
            switch (shipnum){
                case 1:
                    len = 5;
                    break;
                case 2:
                    len = 2;
                    break;
                case 3:
                    len = 3;
                    break;
                case 4:
                    len = 1;
                    break;
                case 5:
                    len = 0; // need to add dialog box;
                    break;
            }

        }
    }
    private void drawnewship(){
        if (!checkdir()){
            drawboard();
            setbtn.setClickable(false);
            places[selected[0]][selected[1]].setImageResource(R.drawable.red);
            return;
        }
        setbtn.setClickable(true);
        switch (dir){
            case 0:
                drawboard();
                for (int i = 0; i < len; i ++){
                    places[selected[0]+i][selected[1]].setImageResource(R.drawable.red);
                }
                return;
            case 1:
                drawboard();
                for (int i = 0; i < len; i ++){
                    places[selected[0]][selected[1]+i].setImageResource(R.drawable.red);
                }
                return;
            case 2:
                drawboard();
                for (int i = 0; i < len; i ++){
                    places[selected[0]-i][selected[1]].setImageResource(R.drawable.red);
                }
                return;
            case 3:
                drawboard();
                for (int i = 0; i < len; i ++){
                    places[selected[0]][selected[1]-i].setImageResource(R.drawable.red);
                }
                return;
        }
    }
    private void drawboard(){
        for (int i = 0 ; i < 10; i++){
            for (int j = 0 ; j < 10; j ++){
                if (board.get(i,j) == 0)
                    places[i][j].setImageResource(R.drawable.sea);
                else if (board.get(i,j) == 1)
                    places[i][j].setImageResource(R.drawable.red);
            }
        }
    }
    private boolean checkdir(){ // returns a boolean which states if you can lay a ship
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

    private void addNew(){
        Ship s = new Ship(len);
        switch (dir){
            case 0:
                drawboard();
                for (int i = 0; i < len; i ++){
                    board.set(selected[0]+i, selected[1], 1);
                    s.addLocation(selected[0]+i, selected[1]);
                }
                board.addShip(s);
                return;
            case 1:
                drawboard();
                for (int i = 0; i < len; i ++){
                    board.set(selected[0], selected[1]+i, 1);
                    s.addLocation(selected[0], selected[1]+i);
                }
                board.addShip(s);
                return;
            case 2:
                drawboard();
                for (int i = 0; i < len; i ++){
                    board.set(selected[0]-i, selected[1], 1);
                    s.addLocation(selected[0]-i, selected[1]);
                }
                board.addShip(s);
                return;
            case 3:
                drawboard();
                for (int i = 0; i < len; i ++){
                    board.set(selected[0], selected[1]-i, 1);
                    s.addLocation(selected[0], selected[1]-i);
                }
                board.addShip(s);
                return;
        }
    }
}
