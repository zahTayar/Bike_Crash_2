package com.example.bikecrash2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bikecrash2.R;
import com.google.android.material.button.MaterialButton;


public class
Activity_Menu extends AppCompatActivity {
    private MaterialButton control_with_sensors;
    private MaterialButton control_with_arrows;
    private MaterialButton record_table;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        control_with_sensors =findViewById(R.id.menu_BTN_sensors);
        control_with_arrows=findViewById(R.id.menu_BTN_arrows);
        record_table=findViewById(R.id.menu_BTN_record_table);
        continue_to_game();

    }

    private void continue_to_game()
    {
        control_with_sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("sensors");
            }
        });

        control_with_arrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("arrows");
            }
        });
        record_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("record");
            }
        });
    }
    private void startGame(String activity)
    {
        if(activity.equals("record")){
            Intent myIntent = new Intent(this, record_table.class);
            Bundle bundle = new Bundle();
            myIntent.putExtra(manageGame.BUNDLE_NAME, bundle);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(this, manageGame.class);
            Bundle bundle = new Bundle();
            bundle.putString(manageGame.ACTIVITY_KIND, activity);
            myIntent.putExtra(manageGame.BUNDLE_NAME, bundle);

            startActivity(myIntent);
        }

    }

}
