package com.krisnaprihadiyanto.tabbedaplication.model;

import com.google.gson.annotations.SerializedName;


public class Trailer extends BaseModel<TrailerData> {

    @SerializedName("id")
    private int id;

    public Trailer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
