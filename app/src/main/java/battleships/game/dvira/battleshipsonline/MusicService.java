package battleships.game.dvira.battleshipsonline;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dvira on 31-Mar-20.
 */

public class MusicService extends Service{
    public static boolean isPlaying;
    public static MediaPlayer musicPlayer;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isPlaying = true;
        musicPlayer = MediaPlayer.create(this, R.raw.soundtrack);
        musicPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        isPlaying = false;
        musicPlayer.stop();
    }
}
