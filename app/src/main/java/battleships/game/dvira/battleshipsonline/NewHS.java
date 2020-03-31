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
import android.widget.Toast;

public class NewHS extends AppCompatActivity implements View.OnClickListener{

    Button submitbtn;
    TextView scoretv;
    EditText nametxt;
    int score;
    ProgressBar pb;
    FirebaseManager fm;
    String badCharsKey;

    Button backbutton;

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

        fm = FirebaseManager.getInstance();

        badCharsKey = ".";

        backbutton = (Button) findViewById(R.id.backBtn3);
        backbutton.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId() == submitbtn.getId()){
            //check name
            for (int i = 0; i < badCharsKey.length(); i++){
                if (nametxt.getText().toString().contains(""+badCharsKey.charAt(i))) {
                    Toast.makeText(this, "BAD NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //save to database
            boolean success = fm.putNewScore(nametxt.getText().toString(), score);
            if (success){
                Toast.makeText(this, "Score Saved!", Toast.LENGTH_SHORT).show();
            } else { //TODO this crashes it, need to fix
                Toast.makeText(NewHS.this, "Name Already Exists! Please Choose Another Name!", Toast.LENGTH_SHORT).show();
                return;
            }

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
                    finish();

                }
            };

            waitthread.start();
        }

        else if (v.getId() == backbutton.getId()){
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
            finish();
        }
    }

}
