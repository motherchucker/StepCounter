package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BestResults extends AppCompatActivity {

    private TextView rezultatiView;
    int lastScore;
    int best1, best2, best3;
    Button reset_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_results);
        rezultatiView = findViewById(R.id.rezultati);
        reset_button = findViewById(R.id.reset_button);


        //ucitavanje prijasnjih rezultata
        final SharedPreferences preferences = getSharedPreferences("PREFS",0);
        lastScore = preferences.getInt("lastScore",0);
        best1 = preferences.getInt("best1",0);
        best2 = preferences.getInt("best2",0);
        best3 = preferences.getInt("best3",0);


        //promjena rezultata
        if(lastScore >= best3){
            best3 = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.apply();
        }
        if(lastScore >= best2){
            int temp = best2;
            best2 = lastScore;
            best3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3 );
            editor.putInt("best2", best2 );
            editor.apply();
        }
        if(lastScore >= best1){
            int temp = best1;
            best1 = lastScore;
            best2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best2", best2 );
            editor.putInt("best1", best1 );
            editor.apply();
        }

        //ispis rezultata
         rezultatiView.setText("1. :" + best1 + "\n" + "2. :" + best2 + "\n" + "3. :" + best3);

    }


}
