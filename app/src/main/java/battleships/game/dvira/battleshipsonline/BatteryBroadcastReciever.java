package battleships.game.dvira.battleshipsonline;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryBroadcastReciever  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);

        if (level < 90){
            Toast.makeText(context, "Your battery is under 90%, since it is at" + level + " %", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Your battery is above 90%, since it is at " + level + " %", Toast.LENGTH_SHORT).show();
        }
    }
}
