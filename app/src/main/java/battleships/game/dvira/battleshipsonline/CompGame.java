package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CompGame extends AppCompatActivity implements View.OnClickListener{

    private ImageButton[][] places;
    private Button homebutton;
    private Button switchbutton;
    protected boolean playerboard;
    private Player player;
    private AutoPlayer complayer;
    private Game game;
    private TextView boardname;
    private Button nxtbutton;
    protected TextView turnnumText;
    private StorageManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        homebutton = (Button) findViewById(R.id.btnhome);
        homebutton.setOnClickListener(this);
        switchbutton = (Button) findViewById(R.id.btnswitch);
        switchbutton.setOnClickListener(this);
        boardname = (TextView) findViewById(R.id.boardname);
        nxtbutton = (Button) findViewById(R.id.btnnext);
        nxtbutton.setOnClickListener(this);
        turnnumText = (TextView) findViewById(R.id.turnnumText);
        places = new ImageButton[10][10];
        playerboard = false;

        sm = new StorageManager(this);

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

        player = (Player) getIntent().getSerializableExtra("player");
        int shipnum = (int) getIntent().getIntExtra("shipnum", 3);
        complayer = new AutoPlayer(10,10, shipnum);
        game = new Game(player, complayer, this);
        drawenemyboard(complayer.getBoard());
    }

    public void onClick(View v){
        if (v.getId() == homebutton.getId()){
            Intent i = new Intent(CompGame.this, battleships.game.dvira.battleshipsonline.Menu.class);
            startActivity(i);
        }

        if (v.getId() == switchbutton.getId()){
            switchBoard();
        }

        for (int i = 0 ; i < 10; i ++){
            for (int j = 0; j < 10; j++){
                if (v.getId() == places[i][j].getId() && game.turn == 0 && !playerboard){ //checks whether a human player can put his choice down
                    //make his choice the block he clicked, and tell the game he clicked
                    int[] ns = {i,j};
                    player.setSelected(ns);
                    game.getTurn();
                    drawenemyboard(complayer.board);
                    game.nextTurn();
                    nxtbutton.setClickable(true);
                }
            }
        }

        if (v.getId() == nxtbutton.getId()){
            if (game.turn == 0){
                switchBoard();
                nxtbutton.setClickable(false);
            } else {
                switchBoard();
                game.nextTurn();
                drawboard(player.board);
            }
        }

    }

    protected void drawboard(Board board){
        for (int i = 0 ; i < 10; i++){
            for (int j = 0 ; j < 10; j ++){
                if (board.get(i,j) == Board.SEA)
                    places[i][j].setImageResource(R.drawable.sea);
                else if (board.get(i,j) == Board.SHIP)
                    places[i][j].setImageResource(R.drawable.black);
                else if (board.get(i,j) == Board.HIT)
                    places[i][j].setImageResource(R.drawable.red);
                else if (board.get(i,j) == Board.MISS)
                    places[i][j].setImageResource(R.drawable.gray);
            }
        }
    }

    protected void drawenemyboard(Board board){
        for (int i = 0 ; i < 10; i++){
            for (int j = 0 ; j < 10; j ++){
                if (board.get(i,j) == Board.SEA)
                    places[i][j].setImageResource(R.drawable.sea);
                else if (board.get(i,j) == Board.SHIP)
                    if (sm.getBoolean("devMode",false))
                        places[i][j].setImageResource(R.drawable.black);
                    else
                        places[i][j].setImageResource(R.drawable.sea);
                else if (board.get(i,j) == Board.HIT)
                    places[i][j].setImageResource(R.drawable.red);
                else if (board.get(i,j) == Board.MISS)
                    places[i][j].setImageResource(R.drawable.gray);
            }
        }
    }

    //switch the boards
    protected void switchBoard(){
        if(playerboard){
            boardname.setText("Opponents Board");
            drawenemyboard(complayer.getBoard());
        } else {
            boardname.setText("Your Board");
            drawboard(player.getBoard());
        }
        playerboard = !playerboard;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.homemenubutton){
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        } else if (id == R.id.mutemenubutton){
            if (Splash.music.isPlaying()) Splash.music.pause();
            else Splash.music.start();
        }
        return true;
    }
}
