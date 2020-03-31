package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity implements View.OnClickListener{
    Button saveBtn;
    StorageManager sm;
    CheckBox musicCheckBox;
    CheckBox soundEffectBox;
    CheckBox devModeBox;
    EditText shipNumBox;
    TextView highscoretext;
    Button resetHighScore;
    TextView momPhoneTextView;
    Button momPhoneButton;

    Button backbutton;

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

        highscoretext = (TextView) findViewById(R.id.highscoreText);
        highscoretext.setText(sm.getInt("highscore",1000)+"");

        resetHighScore = (Button) findViewById(R.id.resetHighscoreBtn);
        resetHighScore.setOnClickListener(this);

        momPhoneTextView = (TextView) findViewById(R.id.momContactTextView);
        momPhoneTextView.setText(sm.getString("momNumber", "054"));

        momPhoneButton = (Button) findViewById(R.id.momContactButton);
        momPhoneButton.setOnClickListener(this);

        backbutton = (Button) findViewById(R.id.backBtn1);
        backbutton.setOnClickListener(this);
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
                stopService(Splash.MusicIntent);
            } else if (musicCheckBox.isChecked() && !sm.getBoolean("soundMusic", true)){
                startService(Splash.MusicIntent);
            }

            sm.setInt("shipnum", shipnum);
            sm.setBoolean("soundMusic", musicCheckBox.isChecked());
            sm.setBoolean("soundEffects", soundEffectBox.isChecked());
            sm.setBoolean("devMode", devModeBox.isChecked());
            sm.setString("momNumber", momPhoneTextView.getText().toString());

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        }

        else if (v.getId() == resetHighScore.getId()){
            sm.setInt("highscore", 1000);
            highscoretext.setText(1000+"");
        }

        else if (v.getId() == momPhoneButton.getId()){
            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(i, 1);
        }

        else if (v.getId() == backbutton.getId()){
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        }
    }

    protected void onActivityResult(int requestC, int resultC, Intent data){
        if (resultC == RESULT_OK && requestC == 1){
            Uri uri = data.getData();

            Cursor cursor = getContentResolver().query(uri, null,null,null,null);
            cursor.moveToFirst();

            int phoneIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            int nameIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            String PhoneN = cursor.getString(phoneIndex);
//            String name = cursor.getString(nameIndex);

            momPhoneTextView.setText(PhoneN);
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
