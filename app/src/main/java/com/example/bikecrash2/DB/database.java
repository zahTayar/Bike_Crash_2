package com.example.bikecrash2.DB;

import java.util.ArrayList;

public class database {

    private ArrayList<user_record> user_records;

    public database() {
        user_records = new ArrayList<>();
    }

    public ArrayList<user_record> getRecords() {
        return user_records;
    }

    public database setRecords(ArrayList<user_record> user_records) {
        this.user_records = user_records;
        return this;
    }

}
