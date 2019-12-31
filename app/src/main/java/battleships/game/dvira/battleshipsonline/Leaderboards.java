package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Leaderboards extends AppCompatActivity  implements View.OnClickListener{

    FirebaseManager fm;
    ListView LeaderBoardsList;
    Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        fm = FirebaseManager.getInstance();


        LeaderBoardsList = (ListView) findViewById(R.id.leaderboardsList);

        if(fm.getScores().size() != 0){
            updateListView();
        } else {
            try {
                fm.updateScores(this::updateListView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        backbutton = (Button) findViewById(R.id.backBtn2);
        backbutton.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId() == backbutton.getId()){
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        }
    }

    public void updateListView(){
        ArrayList<String> leaderboards = new ArrayList<>();
        ArrayList<Map.Entry<String,Integer>> scores = fm.getScores();
        int num = 1;
        for(Map.Entry<String,Integer> e : scores){
            leaderboards.add(num + ". " + e.getKey() + " - " + e.getValue());
            num++;
        }
//        Collections.reverse(leaderboards);

        ArrayAdapter adapter = new ArrayAdapter<String>(Leaderboards.this,R.layout.support_simple_spinner_dropdown_item,leaderboards);
        LeaderBoardsList.setAdapter(adapter);
    }
}
