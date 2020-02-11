package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StepCounterActivity extends AppCompatActivity {

    private TextView textView;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    Button reset_button;
    Button stop_button;
    public int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        textView = findViewById(R.id.textView);
        reset_button = findViewById(R.id.reset_button);
        stop_button = findViewById(R.id.stop_button);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!=null){
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;
            //sensitivity pokreta.. mobitel iz nekog razloga jako slabo detektira pokret, inace je stavljeno na >6
                    if(MagnitudeDelta>1){
                        stepCount++;
                        score++;
                    }
                    textView.setText((stepCount.toString()));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        reset_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                stepCount=0;
            }
        });

        sensorManager.registerListener(stepDetector,sensor,SensorManager.SENSOR_DELAY_NORMAL);

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("lastScore", score);
                editor.apply();
                stepCount=0;
            }
        });
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putInt("stepCount",stepCount);
        editor.apply();
    }
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putInt("stepCount",stepCount);
        editor.apply();
    }

    protected void onResume(){
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount",0);
    }
}
