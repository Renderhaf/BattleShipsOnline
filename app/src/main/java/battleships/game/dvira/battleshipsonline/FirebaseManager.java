package battleships.game.dvira.battleshipsonline;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by dvira on 27-Sep-19.
 */

public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseDatabase mDatabase;
    private ArrayList<Map.Entry<String, Integer>> scoresMap;

    private FirebaseManager(){
        mDatabase = FirebaseDatabase.getInstance();
        scoresMap = new ArrayList<>();
        this.updateScores();

//        //TODO remove this
//        mDatabase.getReference().child("leaderboards").child("Users").child("Testuser").setValue(500);
//        mDatabase.getReference().child("leaderboards").child("Users").child("Testuser2").setValue(75);
//        mDatabase.getReference().child("leaderboards").child("Users").child("Renderhaf").setValue(37);

    }

    public static FirebaseManager getInstance(){
        if (instance == null)
            instance = new FirebaseManager();

        return instance;
    }

    public void updateScores(Runnable callback) throws Exception {
        DatabaseReference mRef = mDatabase.getReference().child("leaderboards").child("Users");
        Query byScore = mRef.orderByValue();
        final Runnable c = callback;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Integer> scores = (Map<String,Integer>) dataSnapshot.getValue();
                scoresMap = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : scores.entrySet()){
                    insertValue(entry);
                }
                try {
                    c.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Cancel", "loadPost:onCancelled", databaseError.toException());
            }
        };
        byScore.addValueEventListener(postListener);
    }

    public void updateScores() {
        DatabaseReference mRef = mDatabase.getReference().child("leaderboards").child("Users");
        Query byScore = mRef.orderByValue();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Integer> scores = (Map<String,Integer>) dataSnapshot.getValue();
                scoresMap = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : scores.entrySet()){
                    insertValue(entry);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Cancel", "loadPost:onCancelled", databaseError.toException());
            }
        };
        byScore.addValueEventListener(postListener);
    }

    public ArrayList<Map.Entry<String,Integer>> getScores(){
        return scoresMap;
    }

    public String[] getUsers(){
        String[] users = new String[scoresMap.size()];
        int i = 0;
        for (Map.Entry<String,Integer> entry : scoresMap){
            users[i] = entry.getKey();
            i++;
        }
        return users;
    }

    public boolean putNewScore(String name, int score){
        String[] users = this.getUsers();
        for (String s : users){
            if (s.compareTo(name) == 0){
                return false;
            }
        }
        mDatabase.getReference().child("leaderboards").child("Users").child(name).setValue(score);
        return true;
    }

    private void insertValue(Map.Entry<String,Integer> e){
        for (int i = 0 ; i < scoresMap.size(); i++){
            int val1 = Integer.parseInt(e.getValue() + "");//TODO understand what the heck is going on here
            int val2 = Integer.parseInt(scoresMap.get(i).getValue() + "");
            if (val1 < val2){
                scoresMap.add(i,e);
                return;
            }
        }

        scoresMap.add(e);
    }

}
