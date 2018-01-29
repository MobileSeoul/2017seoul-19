package com.gangnam4bungate.nuviseoul.model.map;

import com.google.gson.annotations.SerializedName;


/**
 * Created by wsseo on 2017. 7. 2..
 */

public class MapModel {
    @SerializedName("response")
    MapResponse response;

    public MapResponse getResponse() {
        return response;
    }
}
