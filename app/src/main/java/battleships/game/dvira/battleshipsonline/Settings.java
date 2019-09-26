package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity implements View.OnClickListener{
    Button saveBtn;
    StorageManager sm;
    CheckBox musicCheckBox;
    CheckBox soundEffectBox;
    CheckBox devModeBox;
    EditText shipNumBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveBtn = (Button) findViewById(R.id.settingsSaveBtn);
        saveBtn.setOnClickListener(this);

        musicCheckBox = (CheckBox) findViewById(R.id.setMusicCheck);
        soundEffectBox = (CheckBox) findViewById(R.id.setSoundBox);
        devModeBox = (CheckBox) findViewById(R.id.developerModeBox);
        shipNumBox = (EditText) findViewById(R.id.shipNumText);

        sm = new StorageManager(this);

        musicCheckBox.setChecked(sm.getBoolean("soundMusic",true));
        soundEffectBox.setChecked(sm.getBoolean("soundEffects", true));
        devModeBox.setChecked(sm.getBoolean("devMode", false));
        shipNumBox.setText("" + sm.getInt("shipnum",5));
    }

    public void onClick(View v){
        if (v.getId() == saveBtn.getId()){
            //save
            int shipnum = Integer.parseInt(shipNumBox.getText().toString());
            if (shipnum > 5 || shipnum < 1){
                Toast.makeText(this, "Ship number has to be between 1 to 5", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!musicCheckBox.isChecked() && sm.getBoolean("soundMusic", true)){
                Splash.music.stop();
            } else if (musicCheckBox.isChecked() && !sm.getBoolean("soundMusic", true)){
                Splash.music.start();
            }

            sm.setInt("shipnum", shipnum);
            sm.setBoolean("soundMusic", musicCheckBox.isChecked());
            sm.setBoolean("soundEffects", soundEffectBox.isChecked());
            sm.setBoolean("devMode", devModeBox.isChecked());

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        }
    }
}
