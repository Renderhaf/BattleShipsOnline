package battleships.game.dvira.battleshipsonline;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by dvira on 27-Sep-19.
 */

public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseDatabase mDatabase;
    private Map<String, Integer> scoresMap;

    private FirebaseManager(){
        mDatabase = FirebaseDatabase.getInstance();
        scoresMap = new HashMap<>();
        this.updateScores();

//        //TODO remove this
////        scoresMap.put("TEST", 37);
//        mDatabase.getReference().child("leaderboards").child("Users").child("Testuser").setValue("500");
//        mDatabase.getReference().child("leaderboards").child("Users").child("Testuser2").setValue("75");

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
                for (Map.Entry<String, Integer> entry : scores.entrySet()){
                    scoresMap.put(entry.getKey(), entry.getValue());
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
                for (Map.Entry<String, Integer> entry : scores.entrySet()){
                    scoresMap.put(entry.getKey(), entry.getValue());
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

    public int getScore(String name){
        return scoresMap.get(name);
    }

    public Map<String,Integer> getScores(){
        return scoresMap;
    }

    public Object[] getUsers(){
        return scoresMap.keySet().toArray();
    }


    public boolean putNewScore(String name, int score){
        Object[] users = this.getUsers();
        for (Object s : users){
            if (s.toString() == name){
                return false;
            }
        }
        mDatabase.getReference().child("leaderboards").child("Users").setValue(name, score);
        return true;
    }
}
