package battleships.game.dvira.battleshipsonline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dvira on 11-Aug-19.
 */

public class StorageManager {
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public StorageManager(Activity a){
        sp = a.getApplicationContext().getSharedPreferences("battleships.game.dvira.PREFRENCE_FILE", Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    public int getInt(String name, int defaultv){
        return  sp.getInt(name, defaultv);
    }
    public String getString(String name, String defaultv){
        return  sp.getString(name, defaultv);
    }
    public boolean getBoolean(String name, boolean defaultv){
        return  sp.getBoolean(name, defaultv);
    }

    public void setInt(String name, int val){
        editor.putInt(name, val);
        editor.commit();
    }
    public void setString(String name, String val){
        editor.putString(name, val);
        editor.commit();
    }
    public void setBoolean(String name, boolean val){
        editor.putBoolean(name, val);
        editor.commit();
    }
}
