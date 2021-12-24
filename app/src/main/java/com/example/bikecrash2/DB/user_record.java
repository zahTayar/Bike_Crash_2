package com.example.bikecrash2.DB;

import android.widget.TextView;

public class user_record {
    private String nickname;
    private int score;
    private double latlocation;
    private double lnglocation;

    public user_record(){

    }
    public user_record(String nickname, int score, double latlocation,double lnglocation){
        this.nickname=nickname;
        this.score=score;
        this.latlocation=latlocation;
        this.lnglocation=lnglocation;
    }

    public double getLatlocation() {
        return latlocation;
    }

    public user_record setLatlocation(double latlocation) {
        this.latlocation = latlocation;
        return this;
    }

    public double getLnglocation() {
        return lnglocation;
    }

    public user_record setLnglocation(double lnglocation) {
        this.lnglocation = lnglocation;
        return this;
    }

    public int getScore() {
        return score;
    }


    public String getNickname() {
        return nickname;
    }



    public user_record setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public user_record setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        return "" + nickname + "       " + score ;
    }
}
