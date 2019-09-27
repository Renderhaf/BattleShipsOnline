package battleships.game.dvira.battleshipsonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Leaderboards extends AppCompatActivity  implements View.OnClickListener{

    FirebaseManager fm;
    Button getTestData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        fm = FirebaseManager.getInstance();

        getTestData = (Button) findViewById(R.id.getTestDataBtn);
        getTestData.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId() == getTestData.getId()){
            fm.updateScores();
//            Toast.makeText(this, "Renderhafs score is : " + fm.getScore("TEST"), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "First stored score is : " + fm.getUsers()[0].toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
