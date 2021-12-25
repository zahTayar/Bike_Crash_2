package com.example.bikecrash2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bikecrash2.CallBack_List;
import com.example.bikecrash2.DB.SortUserRecordByScore;
import com.example.bikecrash2.DB.database;
import com.example.bikecrash2.DB.MSPV3;
import com.example.bikecrash2.R;
import com.example.bikecrash2.DB.user_record;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_List extends Fragment {

    private final String DB_NAME = "BIKE_CRASH_2_DB";
    private final String defDb = "{\"user_records\":[]}";
    private AppCompatActivity activity;
    private Button[] tableRows;
    private List<user_record> eight_selected =new ArrayList<>();
    private CallBack_List callBackList;
    private final int EIGHT = 8;
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);
        initTable();
        showTable();
        initViews();

        return view;
    }


    private void initTable()
    {
        String db=MSPV3.getMe().getString(DB_NAME,defDb);
        database my_db = new Gson().fromJson(db,database.class);
        ArrayList<user_record> user_records = my_db.getRecords();
        int lisSize = user_records.size();
        Collections.sort(user_records, new SortUserRecordByScore());
        int start=0;
        if(user_records.size()>=EIGHT){
            start = lisSize-10;
        }
        eight_selected = user_records.subList(start, lisSize);
        Collections.reverse(eight_selected);
    }
    private void showTable(){
        for (int i=0;i<eight_selected.size();i++){
            String user=eight_selected.get(i).toString();
            tableRows[i].setText(user);

        }
    }


    private void initViews() {
        int amount_of_user_records=eight_selected.size();
        if (amount_of_user_records>0){
            tableRows[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(0).getLatlocation(),eight_selected.get(0).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>1){
            tableRows[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(1).getLatlocation(),eight_selected.get(1).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>2){
            tableRows[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(2).getLatlocation(),eight_selected.get(2).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>3){
            tableRows[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(3).getLatlocation(),eight_selected.get(3).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>4){
            tableRows[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(4).getLatlocation(),eight_selected.get(4).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>5){
            tableRows[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(5).getLatlocation(),eight_selected.get(5).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>6){
            tableRows[6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(6).getLatlocation(),eight_selected.get(6).getLnglocation());
                }
            });

        }
        if (amount_of_user_records>7){
            tableRows[7].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackList.setLocationInMap(eight_selected.get(7).getLatlocation(),eight_selected.get(7).getLnglocation());
                }
            });

        }
//
//        switch (amount_of_user_records){
//            case 1:
//                tableRows[0].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(0).getLatlocation(),eight_selected.get(0).getLnglocation());
//                    }
//                });
//                break;
//            case 2:
//                tableRows[1].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(1).getLatlocation(),eight_selected.get(1).getLnglocation());
//
//                    }
//                });
//                break;
//            case 3:
//                tableRows[2].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(2).getLatlocation(),eight_selected.get(2).getLnglocation());
//
//                    }
//                });
//                break;
//            case 4:
//                tableRows[3].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(3).getLatlocation(),eight_selected.get(3).getLnglocation());
//
//                    }
//                });
//                break;
//            case 5:
//                tableRows[4].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(4).getLatlocation(),eight_selected.get(4).getLnglocation());
//
//                    }
//                });
//                break;
//            case 6:
//                tableRows[5].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(5).getLatlocation(),eight_selected.get(5).getLnglocation());
//
//                    }
//                });
//                break;
//            case 7:
//                tableRows[6].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(6).getLatlocation(),eight_selected.get(6).getLnglocation());
//
//                    }
//                });
//                break;
//            case 8:
//                tableRows[7].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        callBackList.setLocationInMap(eight_selected.get(7).getLatlocation(),eight_selected.get(7).getLnglocation());
//
//                    }
//                });
//                break;
//        }
    }


    private void findViews(View view)
    {
        tableRows =new Button[]{
        view.findViewById(R.id.btn_table_1),
        view.findViewById(R.id.btn_table_2),
        view.findViewById(R.id.btn_table_3),
        view.findViewById(R.id.btn_table_4),
        view.findViewById(R.id.btn_table_5),
        view.findViewById(R.id.btn_table_6),
        view.findViewById(R.id.btn_table_7),
        view.findViewById(R.id.btn_table_8)
        };
    }
}