package com.example.bikecrash2.DB;

import java.util.Comparator;

public class SortUserRecordByScore implements Comparator<user_record> {

    @Override
    public int compare(user_record t1, user_record t2) {
        if (t1.getScore()!=t2.getScore()){
            return t1.getScore() - t2.getScore();
        }else{
            return t1.getNickname().compareTo(t2.getNickname());
        }
    }


}
