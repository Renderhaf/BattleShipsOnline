package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private ImageView imageView;
    public static MediaPlayer music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.imageView);
        Thread mSlashScreen = new Thread(){
            @Override
            public void run(){
                try{
                    synchronized (this){
                        music = MediaPlayer.create(Splash.this, R.raw.soundtrack);
                        music.start();
                        Animation fade = AnimationUtils.loadAnimation(Splash.this, R.anim.tween);
                        imageView.startAnimation(fade);
                        wait(2000); //10000
                    }}
                catch(InterruptedException e){}
                finish();

                Intent i = new Intent(Splash.this, battleships.game.dvira.battleshipsonline.Menu.class);
                startActivity(i);
            }

        };
        mSlashScreen.start();
    };
}
