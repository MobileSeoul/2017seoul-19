package com.gangnam4bungate.nuviseoul.model.map;

import com.gangnam4bungate.nuviseoul.model.TourCourseInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wsseo on 2017. 7. 6..
 */

public class MapItem {
    @SerializedName("item")
    MapInfo mitem;
    public MapInfo getItem() {
        return mitem;
    }
}
