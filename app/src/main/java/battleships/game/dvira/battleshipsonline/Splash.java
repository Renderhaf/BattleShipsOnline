package battleships.game.dvira.battleshipsonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private ImageView imageView;
    public static Intent MusicIntent;
    public static Context context;
    StorageManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        sm = new StorageManager(this);
        MusicIntent = new Intent(this,  MusicService.class);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.imageView);
        Thread mSlashScreen = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        if (sm.getBoolean("soundMusic",true)) {
                            startService(MusicIntent);
                        }
                        Animation fade = AnimationUtils.loadAnimation(Splash.this, R.anim.tween);
                        imageView.startAnimation(fade);
                        wait(3000); //10000
                    }}
                catch(InterruptedException e){}
                finish();

                Intent i = new Intent(Splash.this, battleships.game.dvira.battleshipsonline.Menu.class);
                startActivity(i);
            }

        };
        mSlashScreen.start();
    }
}
