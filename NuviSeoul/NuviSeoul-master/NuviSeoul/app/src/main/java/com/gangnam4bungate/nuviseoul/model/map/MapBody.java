package com.gangnam4bungate.nuviseoul.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class MapBody {
    @SerializedName("items")
    MapItem mitems;
    public MapItem getItems(){
        return this.mitems;
    }
}
