package com.gangnam4bungate.nuviseoul.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class MapInfo {
    @SerializedName("contentid")
    String contentid;
    @SerializedName("contenttypeid")
    String contenttypeid;
    @SerializedName("mapx")
    String mapx;
    @SerializedName("mapy")
    String mapy;
    @SerializedName("mlevel")
    String mlevel;

    public String getContentid() {
        return contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public String getMapx() {
        return mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public String getMlevel() {
        return mlevel;
    }
}
