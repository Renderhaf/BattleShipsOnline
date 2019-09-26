package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout lay;
    Button settingsButton;
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
    }
    public void onClick(View v){
        if (v.getId() == settingsButton.getId()){
            Intent i = new Intent(Menu.this, Settings.class);
            startActivity(i);
        }
    }
}
