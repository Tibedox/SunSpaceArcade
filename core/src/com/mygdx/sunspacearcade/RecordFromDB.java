package com.mygdx.sunspacearcade;

import com.google.gson.annotations.SerializedName;

public class RecordFromDB {
    @SerializedName("id")
    int id;

    @SerializedName("playername")
    String name;

    @SerializedName("score")
    int score;

    @SerializedName("daterecord")
    String date;
}
