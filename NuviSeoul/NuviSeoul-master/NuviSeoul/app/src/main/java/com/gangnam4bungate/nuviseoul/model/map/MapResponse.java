package com.gangnam4bungate.nuviseoul.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class MapResponse {
    @SerializedName("header")
    MapHeader header;
    @SerializedName("body")
    MapBody body;

    public MapHeader getHeader() {
        return header;
    }
    public MapBody getBody() {
        return body;
    }
}
