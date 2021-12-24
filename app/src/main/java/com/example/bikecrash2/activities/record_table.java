package com.example.bikecrash2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.bikecrash2.CallBack_List;
import com.example.bikecrash2.R;
import com.example.bikecrash2.fragments.Fragment_List;
import com.example.bikecrash2.fragments.Fragment_Map;
import com.google.android.gms.maps.model.LatLng;

public class record_table extends AppCompatActivity {
    public static final String PLAYER_NAME = "PLAYER_NAME";
    public static final String BUNDLE_NAME = "Bundle_record";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    private Fragment_List fragmentList;
    private Fragment_Map fragmentMap;
    private double lat=0;
    private double lng=0;
    private AppCompatButton back;
    String player_name="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_table);
        if(player_name.length()>0){
            player_name=getIntent().getExtras().getBundle(BUNDLE_NAME).getString(PLAYER_NAME);

        }else{
            player_name="default";
        }
        if(lat!=0 && lng!=0){
            lat = getIntent().getExtras().getBundle(BUNDLE_NAME).getDouble(LAT);
            lng = getIntent().getExtras().getBundle(BUNDLE_NAME).getDouble(LNG);
        }

        initForRecordScreen();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void initForRecordScreen()
    {

        back=findViewById(R.id.back_BTN);
        fragmentList = new Fragment_List();
        fragmentList.setActivity(this);
        fragmentList.setCallBackList(callBackList);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, fragmentList).commit();


        fragmentMap = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.frame2, fragmentMap).commit();
        fragmentMap.setDefaultLocation(new LatLng(lat,lng));
    }

    CallBack_List callBackList = new CallBack_List() {

        @Override
        public void setLocationInMap(double lat,double lng) {
            fragmentMap.setLocationOfCurrentUser(new LatLng(lat,lng));
        }
    };
}
