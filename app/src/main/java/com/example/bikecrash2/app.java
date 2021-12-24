package com.example.bikecrash2;

import android.app.Application;
import com.example.bikecrash2.DB.MSPV3;


import com.example.bikecrash2.DB.MSPV3;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSPV3.initHelper(this);
    }
}
