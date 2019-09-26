package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewHS extends AppCompatActivity implements View.OnClickListener{

    Button submitbtn;
    TextView scoretv;
    EditText nametxt;
    int score;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        submitbtn = (Button) findViewById(R.id.subbtn);
        submitbtn.setOnClickListener(this);
        scoretv = (TextView) findViewById(R.id.scoretv);
        nametxt = (EditText) findViewById(R.id.nametxt);
        pb = (ProgressBar)findViewById(R.id.loadDatabasebar);

        score = (int) getIntent().getIntExtra("score", 1000);
        scoretv.setText("Score : " + score);

        nametxt.setHint("Name");
    }

    public void onClick(View v){
        if (v.getId() == submitbtn.getId()){
            //put new highscore in database -----------------------------TODO

            pb.setVisibility(View.VISIBLE);
            final Thread waitthread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(3000);
                        }
                    } catch (InterruptedException e) {
                    }
                    finish();

                    Intent i = new Intent(NewHS.this, Menu.class);
                    startActivity(i);
                }
            };

            waitthread.start();
        }
    }
}
