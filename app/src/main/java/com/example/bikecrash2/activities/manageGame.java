package com.example.bikecrash2.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.io.IOException;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.bikecrash2.DB.SortUserRecordByScore;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.example.bikecrash2.DB.database;
import com.example.bikecrash2.DB.MSPV3;
import com.example.bikecrash2.R;
import com.example.bikecrash2.DB.user_record;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.paz.accesstolib.GiveMe;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class manageGame extends AppCompatActivity {
    private static Timer timer = new Timer();
    GiveMe giveMe;

    MediaPlayer mp;


    ArrayList<ImageView> motors=new ArrayList<>();
    ArrayList <ImageView> hearts=new ArrayList<>();
    ArrayList <ImageView> all_rock_middle=new ArrayList();
    ArrayList <ImageView> all_rock_left=new ArrayList();
    ArrayList <ImageView> all_rock_right=new ArrayList();
    ArrayList <ImageView> all_rock_right_middle=new ArrayList();
    ArrayList <ImageView> all_rock_left_middle=new ArrayList();
    ArrayList <user_record> all_records=new ArrayList<>();


    private SensorManager sensorManager;
    private Sensor accSensor;

    private MaterialButton frame1_BTN_add;
    private TextInputLayout frame1_EDT_nickname;

    private ImageButton button_left;
    private ImageButton button_right;
    private TextView scoreTextView;

    private int bottleOne=0;
    private boolean[][] matrixFlags=new boolean[4][5];
    private int state=3;
    private int i;
    private int count = 0;
    private int random=0,randomForStone1=0,randomForStone2=0,randomForStone3=0;
    public int hearts_number=3;
    private int activity_flag=0;
    public String player_name;
    private int score=0;
    private  double lat, lng;

    private FusedLocationProviderClient fusedLocationProviderClient;


    private final String DB_NAME = "BIKE_CRASH_2_DB";
    private final String defDb = "{\"user_records\":[]}";
    public static final String ACTIVITY_KIND = "ACTIVITY_KIND";
    public static final String BUNDLE_NAME = "Bundle_manage";
    private final int EIGHT = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(manageGame.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(manageGame.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        String activity_kind = getIntent().getExtras().getBundle(BUNDLE_NAME).getString(ACTIVITY_KIND);
        if(activity_kind.equals("sensors")){
            initSensor();
            setContentView(R.layout.activity_sensors);
            activity_flag=0;
            configureOnNewSession(activity_kind);
        }else if(activity_kind.equals("arrows")){
            setContentView(R.layout.activity_arrows);
            activity_flag=1;
            configureOnNewSession(activity_kind);
            manageGame();
        }

    }
    public void configureOnNewSession(String activity_kind)
    {

        if(!activity_kind.equals("record")){
            resetFlags();
            initAllElements();
            changePositionToMotor();
            ImageView imageView = (ImageView) findViewById(R.id.my_image_view);
            Glide
                    .with(this)
                    .load("road.jpg")
                    .into(imageView);
            initialRocks();
            calculateRandomStone();
        }
        frame1_BTN_add.setVisibility(View.INVISIBLE);
        frame1_EDT_nickname.setVisibility(View.INVISIBLE);
    }
    public void manageGame()
    {
        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state>0){
                    state--;
                }
                changePositionToMotor();
            }
        });
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state<5){
                    state++;
                }
                changePositionToMotor();

            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(activity_flag!=2){
            if(count<7)
            {
                handleTimerAndGame();
            }
        }

    }
    public void initialRocks()
    {
        for (i=0; i<all_rock_middle.size();i++)
        {
            all_rock_left.get(i).setTag(R.drawable.rock);
            all_rock_left_middle.get(i).setTag(R.drawable.rock);
            all_rock_middle.get(i).setTag(R.drawable.rock);
            all_rock_right_middle.get(i).setTag(R.drawable.rock);
            all_rock_right.get(i).setTag(R.drawable.rock);
            all_rock_middle.get(i).setVisibility(View.INVISIBLE);
            all_rock_left.get(i).setVisibility(View.INVISIBLE);
            all_rock_left_middle.get(i).setVisibility(View.INVISIBLE);
            all_rock_right_middle.get(i).setVisibility(View.INVISIBLE);
            all_rock_right.get(i).setVisibility(View.INVISIBLE);
        }

    }
    private void initSensor()
    {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    public void checkCountIfDamage()
    {
        if(count==5)
        {
            if(all_rock_left.get(count+2).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count+2).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left.get(count+2).setVisibility(View.INVISIBLE);
            }else if(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count+2).getVisibility()==View.VISIBLE)
            {
                //heart gone
                hearts_number--;
                disapearHeart();
            }else if(all_rock_middle.get(count+2).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count+2).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_middle.get(count+2).setVisibility(View.INVISIBLE);
            }else if(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count+2).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_left_middle.get(count+2).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count+2).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left_middle.get(count+2).setVisibility(View.INVISIBLE);
            }else if(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count+2).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_right_middle.get(count+2).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count+2).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right_middle.get(count+2).setVisibility(View.INVISIBLE);
            }else if(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count+2).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_right.get(count+2).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count+2).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right.get(count+2).setVisibility(View.INVISIBLE);
            }else if(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count+2).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else{
                changeScore(1);
            }
        }
        else if(count==6)
        {
            if(all_rock_left.get(count+1).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count+1).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left.get(count+1).setVisibility(View.INVISIBLE);
            }else
            if(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count+1).getVisibility()==View.VISIBLE)
            {
                //heart gone
                hearts_number--;
                disapearHeart();
            }else if(all_rock_left_middle.get(count+1).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count+1).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left_middle.get(count+1).setVisibility(View.INVISIBLE);
            }else if(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count+1).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_middle.get(count+1).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count+1).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_middle.get(count+1).setVisibility(View.INVISIBLE);
            }else if(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count+1).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_right_middle.get(count+1).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count+1).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right_middle.get(count+1).setVisibility(View.INVISIBLE);
            }else if(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count+1).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_right.get(count+1).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count+1).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right.get(count+1).setVisibility(View.INVISIBLE);
            }else if(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count+1).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else{
                changeScore(1);
            }
        }
        else if(count==7)
        {
            if(all_rock_left.get(count).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left.get(count).setVisibility(View.INVISIBLE);
            }else if(motors.get(0).getVisibility()==View.VISIBLE && all_rock_left.get(count).getVisibility()==View.VISIBLE)
            {
                //heart gone
                hearts_number--;
                disapearHeart();
            }else if(all_rock_left_middle.get(count).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_left_middle.get(count).setVisibility(View.INVISIBLE);
            }else if(motors.get(1).getVisibility()==View.VISIBLE && all_rock_left_middle.get(count).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();

            }else if(all_rock_middle.get(count).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_middle.get(count).setVisibility(View.INVISIBLE);
            }else if(motors.get(2).getVisibility()==View.VISIBLE && all_rock_middle.get(count).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();
            }else if(all_rock_right_middle.get(count).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right_middle.get(count).setVisibility(View.INVISIBLE);
            }else if(motors.get(3).getVisibility()==View.VISIBLE && all_rock_right_middle.get(count).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();

            }else if(all_rock_right.get(count).getTag().equals((Integer)R.drawable.plastic)&&(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count).getVisibility()==View.VISIBLE)){
                changeScore(2);
                all_rock_right.get(count).setVisibility(View.INVISIBLE);
            }else if(motors.get(4).getVisibility()==View.VISIBLE && all_rock_right.get(count).getVisibility()==View.VISIBLE)
            {
                hearts_number--;
                disapearHeart();

            }else{
                changeScore(1);
            }

        }
    }
    public void changePosition_mat()
    {
        for (int i=0;i<4;i++)
        {
            for(int j=0;j<motors.size();j++)
            {
                if(matrixFlags[i][j]==true){
                    changePositionByLocation(i,j);

                }
            }

        }
    }
    public void changePositionByLocation(int row,int col)
    {

        if (count < 6) {
            if (col == 0) {
                if(all_rock_left.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_left.get((row + count) - 1).setImageResource(R.drawable.rock);
                    all_rock_left.get((row + count) - 1).setTag(R.drawable.rock);
                    all_rock_left.get((row + count)).setImageResource(R.drawable.plastic);
                    all_rock_left.get((row + count)).setTag(R.drawable.plastic);
                }
                all_rock_left.get((row + count) - 1).setVisibility(View.INVISIBLE);
                all_rock_left.get((row + count)).setVisibility(View.VISIBLE);
            }
            if (col == 1) {
                if(all_rock_left_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_left_middle.get((row + count) - 1).setImageResource(R.drawable.rock);
                    all_rock_left_middle.get((row + count) - 1).setTag(R.drawable.rock);
                    all_rock_left_middle.get((row + count)).setTag(R.drawable.plastic);
                    all_rock_left_middle.get((row + count)).setImageResource(R.drawable.plastic);
                }
                all_rock_left_middle.get((row + count) - 1).setVisibility(View.INVISIBLE);
                all_rock_left_middle.get((row + count)).setVisibility(View.VISIBLE);

            }
            if (col == 2) {
                if(all_rock_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_middle.get((row + count) - 1).setImageResource(R.drawable.rock);
                    all_rock_middle.get((row + count) - 1).setTag(R.drawable.rock);
                    all_rock_middle.get((row + count)).setTag(R.drawable.plastic);
                    all_rock_middle.get((row + count)).setImageResource(R.drawable.plastic);
                }
                all_rock_middle.get((row + count) - 1).setVisibility(View.INVISIBLE);
                all_rock_middle.get((row + count)).setVisibility(View.VISIBLE);
            }
            if (col == 3) {
                if(all_rock_right_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_right_middle.get((row + count) - 1).setTag(R.drawable.rock);
                    all_rock_right_middle.get((row + count) - 1).setImageResource(R.drawable.rock);
                    all_rock_right_middle.get((row + count)).setTag(R.drawable.plastic);
                    all_rock_right_middle.get((row + count)).setImageResource(R.drawable.plastic);
                }
                all_rock_right_middle.get((row + count) - 1).setVisibility(View.INVISIBLE);
                all_rock_right_middle.get((row + count)).setVisibility(View.VISIBLE);
            }
            if (col == 4) {
                if(all_rock_right.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_right.get((row + count) - 1).setImageResource(R.drawable.rock);
                    all_rock_right.get((row + count) - 1).setTag(R.drawable.rock);
                    all_rock_right.get((row + count)).setTag(R.drawable.plastic);
                    all_rock_right.get((row + count)).setImageResource(R.drawable.plastic);
                }
                all_rock_right.get((row + count) - 1).setVisibility(View.INVISIBLE);
                all_rock_right.get((row + count)).setVisibility(View.VISIBLE);
            }

        }
        else {
            if (count == 6) {
                resetRocks(7, false);
                resetRocks(1, true);
            }
            if (col == 0) {
                if(all_rock_left.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_left.get(count - 1).setImageResource(R.drawable.rock);
                    all_rock_left.get(count).setImageResource(R.drawable.plastic);
                }
                all_rock_left.get(count - 1).setVisibility(View.INVISIBLE);
                all_rock_left.get(count - 1).setTag(View.INVISIBLE);
                all_rock_left.get(count).setTag(View.VISIBLE);
                all_rock_left.get(count).setVisibility(View.VISIBLE);
            }
            if (col == 1) {
                if(all_rock_left_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_left_middle.get(count - 1).setTag(R.drawable.rock);
                    all_rock_left_middle.get(count).setTag(R.drawable.plastic);
                }
                all_rock_left_middle.get(count - 1).setVisibility(View.INVISIBLE);
                all_rock_left_middle.get(count).setVisibility(View.VISIBLE);
            }
            if (col == 2) {
                if(all_rock_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_middle.get(count - 1).setImageResource(R.drawable.rock);
                    all_rock_middle.get(count - 1).setTag(R.drawable.rock);
                    all_rock_middle.get(count).setTag(R.drawable.plastic);
                    all_rock_middle.get(count).setImageResource(R.drawable.plastic);
                }
                all_rock_middle.get(count - 1).setVisibility(View.INVISIBLE);
                all_rock_middle.get(count).setVisibility(View.VISIBLE);
            }
            if (col == 3) {
                if(all_rock_right_middle.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_right_middle.get(count - 1).setTag(R.drawable.rock);
                    all_rock_right_middle.get(count - 1).setImageResource(R.drawable.rock);
                    all_rock_right_middle.get(count).setTag(R.drawable.plastic);
                    all_rock_right_middle.get(count).setImageResource(R.drawable.plastic);
                }
                all_rock_right_middle.get(count - 1).setVisibility(View.INVISIBLE);
                all_rock_right_middle.get(count).setVisibility(View.VISIBLE);
            }
            if (col == 4) {
                if(all_rock_right.get((row + count)-1).getTag().equals(R.drawable.plastic)){
                    all_rock_right.get(count - 1).setImageResource(R.drawable.rock);
                    all_rock_right.get(count - 1).setTag(R.drawable.rock);
                    all_rock_right.get(count).setTag(R.drawable.plastic);
                    all_rock_right.get(count).setImageResource(R.drawable.plastic);
                }
                all_rock_right.get(count - 1).setVisibility(View.INVISIBLE);
                all_rock_right.get(count).setVisibility(View.VISIBLE);
            }
        }
    }
    public void handleTimerAndGame()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count<8){
                            changePosition_mat();
                            checkCountIfDamage();

                        }else{
                            resetRocks(7,false);
                            resetFlags();
                            count=0;
                            calculateRandomStone();
                        }
                    }
                });
            }
        }, 0, 1000);

    }
    public void resetFlags()
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<motors.size();j++)
            {
                matrixFlags[i][j]=false;
            }
        }
    }
    public void resetRocks(int index,boolean inArray)
    {
        if(inArray){
            for(int i=0;i<5;i++){
                matrixFlags[2][i]=false;
            }
        }else{
            all_rock_right.get(index).setTag(R.drawable.rock);
            all_rock_right_middle.get(index).setTag(R.drawable.rock);
            all_rock_middle.get(index).setTag(R.drawable.rock);
            all_rock_left_middle.get(index).setTag(R.drawable.rock);
            all_rock_left.get(index).setTag(R.drawable.rock);
            all_rock_right.get(index).setImageResource(R.drawable.rock);
            all_rock_right_middle.get(index).setImageResource(R.drawable.rock);
            all_rock_middle.get(index).setImageResource(R.drawable.rock);
            all_rock_left_middle.get(index).setImageResource(R.drawable.rock);
            all_rock_left.get(index).setImageResource(R.drawable.rock);
            all_rock_middle.get(index).setVisibility(View.INVISIBLE);
            all_rock_left.get(index).setVisibility(View.INVISIBLE);
            all_rock_left_middle.get(index).setVisibility(View.INVISIBLE);
            all_rock_right.get(index).setVisibility(View.INVISIBLE);
            all_rock_right_middle.get(index).setVisibility(View.INVISIBLE);
        }
    }
    public void massage(boolean flagGameOver)
    {
        CharSequence text = "Ouch you got Crashed";
        if(flagGameOver){
            text = "Game Over !!\n"+"See you next time :)";
        }
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;


        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        giveMe.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        giveMe.onActivityResult(requestCode, resultCode, data);
    }
    public void gameOver()
    {
        findViewById(R.id.gameOverText).setVisibility(View.VISIBLE);
        //finish all tasks
        timer.cancel();
        //put a nice massage
        massage(true);
        frame1_EDT_nickname.setVisibility(View.VISIBLE);
        frame1_BTN_add.setVisibility(View.VISIBLE);

        frame1_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the name
                player_name=getName();

                finish();
                update_db();

                record(player_name);
            }
        });

    }
    public void update_db()
    {
        String str_db=MSPV3.getMe().getString(DB_NAME,defDb);
        database my_db = new Gson().fromJson(str_db,database.class);
        user_record tmp=new user_record().setNickname(player_name).setScore(score).setLatlocation(lat).setLnglocation(lng);
        addRecordToDbWithCheck(tmp,my_db.getRecords());
        String saveJson=new Gson().toJson(my_db);
        MSPV3.getMe().putString(DB_NAME,saveJson);
    }
    private void addRecordToDbWithCheck(user_record r, ArrayList<user_record> user_records)
    {
        user_records.add(r);
        if (user_records.size() <= EIGHT) {
            return;
        }
        Collections.sort(user_records, new SortUserRecordByScore());
        user_records.remove(0);
    }
    public void record(String player_name)
    {
        Intent myIntent = new Intent(this, record_table.class);
        Bundle bundle = new Bundle();
        bundle.putString(record_table.PLAYER_NAME, player_name);
        bundle.putDouble(record_table.LAT,lat);
        bundle.putDouble(record_table.LNG,lng);
        myIntent.putExtra(record_table.BUNDLE_NAME, bundle);

        startActivity(myIntent);
    }
    public void disapearHeart()
    {
        if(hearts.get(2).getVisibility()==View.VISIBLE){
            //vibration
            mp.start();
            //toast massage
            massage(false);
            hearts.get(2).setVisibility(View.INVISIBLE);
        }else if(hearts.get(1).getVisibility()==View.VISIBLE){
            hearts.get(1).setVisibility(View.INVISIBLE);
            mp.start();
            massage(false);
        }else{
            hearts.get(0).setVisibility(View.INVISIBLE);
            mp.start();
            massage(false);
            gameOver();
        }
    }
    public void calculateRandomStone()
    {
        for(int i=0;i<4;i++)
        {
            if(i%2==0)
            {
                giveMeThreeNumbers();
                resetRocks(i,false);
                if(randomForStone1==0 || randomForStone2==0 || randomForStone3==0)
                {
                    all_rock_left.get(i).setVisibility(View.VISIBLE);
                    matrixFlags[i][0]=true;
                }else
                    bottleOne=0;
                if(randomForStone1==1 || randomForStone2==1 || randomForStone3==1)
                {
                    all_rock_left_middle.get(i).setVisibility(View.VISIBLE);
                    matrixFlags[i][1]=true;
                }else
                    bottleOne=1;

                if(randomForStone1==2 || randomForStone2==2 || randomForStone3==2)
                {
                    all_rock_middle.get(i).setVisibility(View.VISIBLE);
                    matrixFlags[i][2]=true;
                }else
                    bottleOne=2;

                if(randomForStone1==3 || randomForStone2==3 || randomForStone3==3)
                {
                    all_rock_right_middle.get(i).setVisibility(View.VISIBLE);
                    matrixFlags[i][3]=true;
                }else
                    bottleOne=3;

                if(randomForStone1==4 || randomForStone2==4 || randomForStone3==4)
                {
                    all_rock_right.get(i).setVisibility(View.VISIBLE);
                    matrixFlags[i][4]=true;
                }else
                    bottleOne=4;
                matrixFlags[i][bottleOne]=true;
                if(bottleOne==0) {
                    all_rock_left.get(i).setTag(R.drawable.plastic);
                    all_rock_left.get(i).setImageResource(R.drawable.plastic);
                    all_rock_left.get(i).setVisibility(View.VISIBLE);
                }else if(bottleOne==1){
                    all_rock_left_middle.get(i).setTag(R.drawable.plastic);
                    all_rock_left_middle.get(i).setImageResource(R.drawable.plastic);
                    all_rock_left_middle.get(i).setVisibility(View.VISIBLE);
                }else if(bottleOne==2) {
                    all_rock_middle.get(i).setTag(R.drawable.plastic);
                    all_rock_middle.get(i).setImageResource(R.drawable.plastic);
                    all_rock_middle.get(i).setVisibility(View.VISIBLE);
                }else if(bottleOne==3){
                    all_rock_right_middle.get(i).setTag(R.drawable.plastic);
                    all_rock_right_middle.get(i).setImageResource(R.drawable.plastic);
                    all_rock_right_middle.get(i).setVisibility(View.VISIBLE);
                }else if(bottleOne==4) {
                    all_rock_right.get(i).setTag(R.drawable.plastic);
                    all_rock_right.get(i).setImageResource(R.drawable.plastic);
                    all_rock_right.get(i).setVisibility(View.VISIBLE);
                }

            }
            else
            {
                resetRocks(i,false);
            }
        }
    }
    public void changeScore(int kind)
    {
        if(kind==1){
            score+=5;
        }else if(kind==2){
            score+=10;
        }
        String str = String.valueOf(score);
        scoreTextView.setText(str);
    }
    public void giveMeThreeNumbers()
    {
        randomForStone1 = new Random().nextInt(5) ;
        randomForStone2 = new Random().nextInt(5) ;
        while(randomForStone1==randomForStone2)
        {
            randomForStone2 = new Random().nextInt(5) ;
        }
        randomForStone3 = new Random().nextInt(5) ;
        while(randomForStone2==randomForStone3 || randomForStone3==randomForStone1)
        {
            randomForStone3 = new Random().nextInt(5);
        }

    }
    public void initAllElements()
    {
        count = 0;
        mp= MediaPlayer.create(getApplicationContext(), R.raw.combo);

        findViewById(R.id.gameOverText).setVisibility(View.INVISIBLE);
        scoreTextView=findViewById(R.id.arrows_TV_score);
        //hearts
        hearts.add(findViewById(R.id.panel_IMG_heart1));
        hearts.add(findViewById(R.id.panel_IMG_heart2));
        hearts.add(findViewById(R.id.panel_IMG_heart3));
        //motor
        motors.add(findViewById(R.id.motor_left));
        motors.add(findViewById(R.id.motor_left_middle));
        motors.add(findViewById(R.id.motor_middle));
        motors.add(findViewById(R.id.motor_right_middle));
        motors.add(findViewById(R.id.motor_right));
        //left
        all_rock_left.add(findViewById(R.id.rock_left1));
        all_rock_left.add(findViewById(R.id.rock_left2));
        all_rock_left.add(findViewById(R.id.rock_left3));
        all_rock_left.add(findViewById(R.id.rock_left4));
        all_rock_left.add(findViewById(R.id.rock_left5));
        all_rock_left.add(findViewById(R.id.rock_left6));
        all_rock_left.add(findViewById(R.id.rock_left7));
        all_rock_left.add(findViewById(R.id.rock_left8));
        //left middle
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle1));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle2));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle3));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle4));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle5));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle6));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle7));
        all_rock_left_middle.add(findViewById(R.id.rock_left_middle8));
        //middle
        all_rock_middle.add(findViewById(R.id.rock_middle1));
        all_rock_middle.add(findViewById(R.id.rock_middle2));
        all_rock_middle.add(findViewById(R.id.rock_middle3));
        all_rock_middle.add(findViewById(R.id.rock_middle4));
        all_rock_middle.add(findViewById(R.id.rock_middle5));
        all_rock_middle.add(findViewById(R.id.rock_middle6));
        all_rock_middle.add(findViewById(R.id.rock_middle7));
        all_rock_middle.add(findViewById(R.id.rock_middle8));
        //right middle
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle1));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle2));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle3));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle4));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle5));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle6));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle7));
        all_rock_right_middle.add(findViewById(R.id.rock_right_middle8));
        //right
        all_rock_right.add(findViewById(R.id.rock_right1));
        all_rock_right.add(findViewById(R.id.rock_right2));
        all_rock_right.add(findViewById(R.id.rock_right3));
        all_rock_right.add(findViewById(R.id.rock_right4));
        all_rock_right.add(findViewById(R.id.rock_right5));
        all_rock_right.add(findViewById(R.id.rock_right6));
        all_rock_right.add(findViewById(R.id.rock_right7));
        all_rock_right.add(findViewById(R.id.rock_right8));

        //buttons
        button_left= findViewById(R.id.button_right_arrow);
        button_right= findViewById(R.id.button_left_arrow);

        frame1_EDT_nickname=findViewById(R.id.frame1_EDT_nickname);
        frame1_BTN_add=findViewById(R.id.frame1_BTN_add);




    }
    public void changePositionToMotor()
    {

        if (state==3){//middle
            motors.get(0).setVisibility(View.INVISIBLE);
            motors.get(1).setVisibility(View.INVISIBLE);
            motors.get(2).setVisibility(View.VISIBLE);
            motors.get(3).setVisibility(View.INVISIBLE);
            motors.get(4).setVisibility(View.INVISIBLE);
        }else if(state==2){//left_middle
            motors.get(0).setVisibility(View.INVISIBLE);
            motors.get(1).setVisibility(View.VISIBLE);
            motors.get(2).setVisibility(View.INVISIBLE);
            motors.get(3).setVisibility(View.INVISIBLE);
            motors.get(4).setVisibility(View.INVISIBLE);
        }else if(state==1){//left
            motors.get(0).setVisibility(View.VISIBLE);
            motors.get(1).setVisibility(View.INVISIBLE);
            motors.get(2).setVisibility(View.INVISIBLE);
            motors.get(3).setVisibility(View.INVISIBLE);
            motors.get(4).setVisibility(View.INVISIBLE);
        }else if(state==4){//right middle
            motors.get(0).setVisibility(View.INVISIBLE);
            motors.get(1).setVisibility(View.INVISIBLE);
            motors.get(2).setVisibility(View.INVISIBLE);
            motors.get(3).setVisibility(View.VISIBLE);
            motors.get(4).setVisibility(View.INVISIBLE);
        }else if(state==5){//right
            motors.get(0).setVisibility(View.INVISIBLE);
            motors.get(1).setVisibility(View.INVISIBLE);
            motors.get(2).setVisibility(View.INVISIBLE);
            motors.get(3).setVisibility(View.INVISIBLE);
            motors.get(4).setVisibility(View.VISIBLE);
        }
    }
    private SensorEventListener accSensorEventListener = new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent event) {
            DecimalFormat df = new DecimalFormat("##.##");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            if(event.values[0]>-1 && event.values[2]>9){
                state=3;
                changePositionToMotor();
            }else if(event.values[0]<-2.5 && event.values[0] > -5 ){
                state=2;
                changePositionToMotor();
            }
            else if(event.values[0]<-5 ){
                state=1;
                changePositionToMotor();
            }else if(event.values[0]>2.5 && event.values[0]<5){
                state=4;
                changePositionToMotor();
            }
            else if(event.values[0]>5 ){
                state=5;
                changePositionToMotor();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    @Override
    protected void onResume()
    {
        super.onResume();
        if(activity_flag==0){
            sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if(activity_flag==0)
        {
            sensorManager.unregisterListener(accSensorEventListener);
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(manageGame.this,
                            Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1
                    );
                    lat = addressList.get(0).getLatitude();
                    lng = addressList.get(0).getLongitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String getName() {
        return frame1_EDT_nickname.getEditText().getText().toString();
    }



}

