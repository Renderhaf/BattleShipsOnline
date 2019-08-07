package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    ConstraintLayout lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
}
