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

    }

    //since the manager is a singleton, you can get its static instance from this function
    public static FirebaseManager getInstance(){
        if (instance == null)
            instance = new FirebaseManager();

        return instance;
    }

    //update the player information, then call a callback
    public void updateScores(Runnable callback) throws Exception {
        DatabaseReference mRef = mDatabase.getReference().child("leaderboards").child("Users");
        Query byScore = mRef.orderByValue();
        //makes the callback final so that the inner class can call it
        final Runnable c = callback;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Integer> scores = (Map<String,Integer>) dataSnapshot.getValue();
                scoresMap = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : scores.entrySet()){
                    //put the entries one by one into the list (so that they can be ordered easily)
                    insertValue(entry);
                }
                try {
                    //run the callback
                    c.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cleanData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Cancel", "loadPost:onCancelled", databaseError.toException());
            }
        };
        byScore.addValueEventListener(postListener);
    }

    //update scores without a callback
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
                cleanData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Cancel", "loadPost:onCancelled", databaseError.toException());
            }
        };
        byScore.addValueEventListener(postListener);
    }

    //returned the scores saved
    public ArrayList<Map.Entry<String,Integer>> getScores(){
        return scoresMap;
    }

    //gets the user list from the scores map
    public String[] getUsers(){
        String[] users = new String[scoresMap.size()];
        int i = 0;
        for (Map.Entry<String,Integer> entry : scoresMap){
            users[i] = entry.getKey();
            i++;
        }
        return users;
    }

    //put a new score into the database, if the name is not there yet
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

    //put one value into the list, so that it is sorted
    private void insertValue(Map.Entry<String,Integer> e){
        for (int i = 0 ; i < scoresMap.size(); i++){
            int val1 = Integer.parseInt(e.getValue() + "");//TODO review this
            int val2 = Integer.parseInt(scoresMap.get(i).getValue() + "");
            if (val1 < val2){
                scoresMap.add(i,e);
                return;
            }
        }

        scoresMap.add(e);
    }

    //find the worst player on the leaderboard, then erase it
    private void cleanData(){
        String key = "";
        while (scoresMap.size() > 10){
            key = scoresMap.get(scoresMap.size()-1).getKey();
            mDatabase.getReference().child("leaderboards").child("Users").child(key).removeValue();
            scoresMap.remove(scoresMap.get(scoresMap.size()-1));
        }
    }

}
