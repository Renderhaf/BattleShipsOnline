package battleships.game.dvira.battleshipsonline;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.BatteryManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout lay;
    Button settingsButton;
    Button leaderboardsbtn;
    Button instructionsbtn;
    Button exitbtn;
    Button playbtn;
    public final BroadcastReceiver batteryBroadCast = new BatteryBroadcastReciever();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu);
        lay = (ConstraintLayout) findViewById(R.id.clayout);

        lay.setOnTouchListener(new OnSwipeTouchListener(Menu.this){
            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
            }

            public void onSwipeTop() {
            }

            public void onSwipeBottom() {
                Intent i = new Intent(Menu.this, MakeBoard.class);
                startActivity(i);
            }
        });

        settingsButton = (Button) findViewById(R.id.settingsBtn);
        settingsButton.setOnClickListener(this);

        leaderboardsbtn = (Button) findViewById(R.id.leaderoardsBtn);
        leaderboardsbtn.setOnClickListener(this);

        instructionsbtn = (Button) findViewById(R.id.instructionsBtn);
        instructionsbtn.setOnClickListener(this);

        exitbtn = (Button) findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(this);

        playbtn = (Button) findViewById(R.id.playbutton);
        playbtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        registerReceiver(batteryBroadCast, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryBroadCast);
        super.onPause();
    }

    public void onClick(View v){
        if (v.getId() == settingsButton.getId()){
            Intent i = new Intent(Menu.this, Settings.class);
            startActivity(i);
            finish();
        }
        else if (v.getId() == leaderboardsbtn.getId()){
            Intent i = new Intent(Menu.this, Leaderboards.class);
            startActivity(i);
            finish();
        }
        else if (v.getId() == instructionsbtn.getId()){
            Intent i = new Intent(Menu.this, Instructions.class);
            startActivity(i);
            finish();
        }
        else if (v.getId() == exitbtn.getId()){
            finish();
            System.exit(0);
        }
        else if (v.getId() == playbtn.getId()){
            Intent i = new Intent(Menu.this, MakeBoard.class);
            startActivity(i);
            finish();
        }
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
            if (MusicService.isPlaying) stopService(Splash.MusicIntent);
            else startService(Splash.MusicIntent);
        }
        return true;
    }
}
