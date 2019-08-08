package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CompGame extends AppCompatActivity implements View.OnClickListener{

    private ImageButton[][] places;
    private Button homebutton;
    private Button switchbutton;
    private boolean playerboard;
    private Player player;
    private AutoPlayer complayer;
    private Game game;
    private TextView boardname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_game);

        homebutton = (Button) findViewById(R.id.btnhome);
        homebutton.setOnClickListener(this);
        switchbutton = (Button) findViewById(R.id.btnswitch);
        switchbutton.setOnClickListener(this);
        boardname = (TextView) findViewById(R.id.boardname);
        places = new ImageButton[10][10];
        playerboard = false;

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
        complayer = new AutoPlayer();
        game = new Game(player, complayer);
        drawboard(complayer.getBoard());
    }

    public void onClick(View v){
        if (v.getId() == homebutton.getId()){
            Intent i = new Intent(CompGame.this, battleships.game.dvira.battleshipsonline.Menu.class);
            startActivity(i);
        }

        if (v.getId() == switchbutton.getId()){
            if(playerboard){
                boardname.setText("Opponents Board");
                drawboard(complayer.getBoard());
            } else {
                boardname.setText("Your Board");
                drawboard(player.getBoard());
            }
            playerboard = !playerboard;
        }

    }

    private void drawboard(Board board){
        for (int i = 0 ; i < 10; i++){
            for (int j = 0 ; j < 10; j ++){
                if (board.get(i,j) == 0)
                    places[i][j].setImageResource(R.drawable.sea);
                else if (board.get(i,j) == 1)
                    places[i][j].setImageResource(R.drawable.red);
            }
        }
    }
}
